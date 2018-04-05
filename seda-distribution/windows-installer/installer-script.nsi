# Modern UI example script
!include MUI.nsh
!include UAC.nsh
Name "SEDA"
OutFile "seda-windows-64b-${SEDA_VERSION}.exe"
InstallDir "$PROGRAMFILES\SEDA"

;Get installation folder from registry if available
InstallDirRegKey HKCU "Software\SEDA" ""

;Request application privileges for Windows Vista
RequestExecutionLevel user

;--------- UAC STUFF ------------
!macro Init thing
uac_tryagain:
!insertmacro UAC_RunElevated
${Switch} $0
${Case} 0
	${IfThen} $1 = 1 ${|} Quit ${|} ;we are the outer process, the inner process has done its work, we are done
	${IfThen} $3 <> 0 ${|} ${Break} ${|} ;we are admin, let the show go on
	${If} $1 = 3 ;RunAs completed successfully, but with a non-admin user
		MessageBox mb_YesNo|mb_IconExclamation|mb_TopMost|mb_SetForeground "This ${thing} requires admin privileges, try again" /SD IDNO IDYES uac_tryagain IDNO 0
	${EndIf}
	;fall-through and die
${Case} 1223
	MessageBox mb_IconStop|mb_TopMost|mb_SetForeground "This ${thing} requires admin privileges, aborting!"
	Quit
${Case} 1062
	MessageBox mb_IconStop|mb_TopMost|mb_SetForeground "Logon service not running, aborting!"
	Quit
${Default}
	MessageBox mb_IconStop|mb_TopMost|mb_SetForeground "Unable to elevate, error $0"
	Quit
${EndSwitch}
 
SetShellVarContext all
!macroend
 
Function .onInit
!insertmacro Init "installer"
FunctionEnd
 
Function un.onInit
!insertmacro Init "uninstaller"
FunctionEnd
;--------- END UAC STUFF ------------


Var StartMenuFolder

!define MUI_ABORTWARNING
!define MUI_ICON "seda.ico"
!define MUI_HEADERIMAGE
!define MUI_HEADERIMAGE_BITMAP "seda-header.bmp"
!define MUI_HEADERIMAGE_RIGHT
!define MUI_WELCOMEFINISHPAGE_BITMAP "installer-splash.bmp"

!insertmacro MUI_PAGE_WELCOME
!insertmacro MUI_PAGE_LICENSE "license.txt"
!insertmacro MUI_PAGE_DIRECTORY
;Start Menu Folder Page Configuration
!define MUI_STARTMENUPAGE_REGISTRY_ROOT "HKCU" 
!define MUI_STARTMENUPAGE_REGISTRY_KEY "Software\SEDA" 
!define MUI_STARTMENUPAGE_REGISTRY_VALUENAME "Start Menu Folder"

  
!insertmacro MUI_PAGE_STARTMENU Application $StartMenuFolder
 
!insertmacro MUI_PAGE_COMPONENTS
!insertmacro MUI_PAGE_INSTFILES

Function LaunchSEDA
  ExecShell "" "$SMPROGRAMS\$StartMenuFolder\SEDA.lnk"
FunctionEnd

!define MUI_FINISHPAGE_RUN
!define MUI_FINISHPAGE_RUN_TEXT "Run SEDA"
!define MUI_FINISHPAGE_RUN_FUNCTION "LaunchSEDA"
!insertmacro MUI_PAGE_FINISH
!insertmacro MUI_LANGUAGE "English"

Section "Extract SEDA"
  SectionIn RO

  RMDir /r "$INSTDIR\windows"
  RMDir /r "$INSTDIR\jars"
  
  RMDir /r "$INSTDIR\plugins_install"
  
  SetOutPath $INSTDIR
  File seda.exe
  File /r ..\windows\64b\jre1.8.0_111
  File /r ..\jars
  
  ;Store installation folder
  WriteRegStr HKCU "Software\SEDA" "" $INSTDIR
  
  ;Create uninstaller
  WriteUninstaller "$INSTDIR\Uninstall.exe"
  
  ;Add to add/remove programs registry entries
  WriteRegStr HKCU "Software\Microsoft\Windows\CurrentVersion\Uninstall\SEDA" "DisplayName" "SEDA"
  WriteRegStr HKCU "Software\Microsoft\Windows\CurrentVersion\Uninstall\SEDA" "UninstallString" "$\"$INSTDIR\Uninstall.exe$\""
  WriteRegStr HKCU "Software\Microsoft\Windows\CurrentVersion\Uninstall\SEDA" "QuietUninstallString" "$\"$INSTDIR\Uninstall.exe$\" /S"
  WriteRegStr HKCU "Software\Microsoft\Windows\CurrentVersion\Uninstall\SEDA" "DisplayIcon" "$\"$INSTDIR\seda.exe$\""
  WriteRegStr HKCU "Software\Microsoft\Windows\CurrentVersion\Uninstall\SEDA" "Publisher" "SING group"
  WriteRegStr HKCU "Software\Microsoft\Windows\CurrentVersion\Uninstall\SEDA" "HelpLink" "http://www.sing-group.org/seda"
  WriteRegStr HKCU "Software\Microsoft\Windows\CurrentVersion\Uninstall\SEDA" "URLInfoAbout" "http://www.sing-group.org/seda"
  WriteRegStr HKCU "Software\Microsoft\Windows\CurrentVersion\Uninstall\SEDA" "DisplayVersion" "${SEDA_VERSION}"
  WriteRegStr HKCU "Software\Microsoft\Windows\CurrentVersion\Uninstall\SEDA" "NoModify" 1
  WriteRegStr HKCU "Software\Microsoft\Windows\CurrentVersion\Uninstall\SEDA" "NoRepair" 1
  
	!insertmacro MUI_STARTMENU_WRITE_BEGIN Application	
  CreateDirectory "$SMPROGRAMS\$StartMenuFolder"
  CreateShortcut "$SMPROGRAMS\$StartMenuFolder\Uninstall.lnk" "$INSTDIR\Uninstall.exe"
  CreateShortcut "$SMPROGRAMS\$StartMenuFolder\SEDA.lnk" "$INSTDIR\seda.exe"
  !insertmacro MUI_STARTMENU_WRITE_END
SectionEnd

Section "Uninstall"

  RMDir /r "$INSTDIR\jars"  
  RMDir /r "$INSTDIR\jre1.8.0_111"
  
  Delete "$INSTDIR\Uninstall.exe"
  Delete "$INSTDIR\seda.exe"

  RMDir "$INSTDIR"
  
  !insertmacro MUI_STARTMENU_GETFOLDER Application $StartMenuFolder
    
  Delete "$SMPROGRAMS\$StartMenuFolder\Uninstall.lnk"
  Delete "$SMPROGRAMS\$StartMenuFolder\SEDA.lnk"

  RMDir /r "$SMPROGRAMS\$StartMenuFolder"
  DeleteRegKey /ifempty HKCU "Software\SEDA"
  
  ;Delete add/remove programs registry entry
  DeleteRegKey HKCU "Software\Microsoft\Windows\CurrentVersion\Uninstall\SEDA"

SectionEnd