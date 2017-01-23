package org.sing_group.seda.gui;

import static java.util.Arrays.stream;

import java.awt.Component;
import java.io.File;
import java.nio.file.Path;
import java.util.function.Consumer;
import java.util.function.IntConsumer;

import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFormattedTextField;
import javax.swing.JSpinner;
import javax.swing.JSpinner.NumberEditor;
import javax.swing.text.DefaultFormatter;

public final class GuiUtils {
	private GuiUtils() {}

	public static void bindCheckBox(JCheckBox chk, Consumer<Boolean> setter) {
		chk.addItemListener(event -> setter.accept(chk.isSelected()));
	}

	public static void bindSpinner(JSpinner spn, IntConsumer setter) {
		final NumberEditor editor = (NumberEditor) spn.getEditor();
		final JFormattedTextField txtField = editor.getTextField();
		final DefaultFormatter formatter = (DefaultFormatter) txtField.getFormatter();
		formatter.setCommitsOnValidEdit(true);
		
		spn.addChangeListener(event -> setter.accept((Integer) spn.getValue()));
	}

	public static void showFileChooserAndProcess(JFileChooser fileChooser, Component parent, int selectionMode, boolean multipleSelection, Consumer<Path> pathProcessor) {
		fileChooser.setFileSelectionMode(selectionMode);
		fileChooser.setMultiSelectionEnabled(multipleSelection);
	
		final int option = fileChooser.showOpenDialog(parent);
		if (option == JFileChooser.APPROVE_OPTION) {
			if (multipleSelection) {
				stream(fileChooser.getSelectedFiles())
					.map(File::toPath)
				.forEach(pathProcessor);
			} else {
				pathProcessor.accept(fileChooser.getSelectedFile().toPath());
			}
		}
	}
	
}
