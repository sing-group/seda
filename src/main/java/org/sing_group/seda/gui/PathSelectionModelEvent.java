package org.sing_group.seda.gui;

import static java.util.Objects.requireNonNull;

import java.util.Optional;

public class PathSelectionModelEvent {
	public static enum FileSelectionEventType {
		ADD_AVAILABLE, ADD_SELECTED,
		REMOVE_AVAILABLE, REMOVE_SELECTED,
		CLEAR_AVAILABLE, CLEAR_SELECTED;
		
		public boolean shouldHavePath() {
			return this == ADD_AVAILABLE ||
				this == ADD_SELECTED ||
				this == REMOVE_AVAILABLE ||
				this == REMOVE_SELECTED;
		}
	}

	private final FileSelectionEventType type;
	private final String path;
	private final int index;
	
	public static PathSelectionModelEvent of(FileSelectionEventType type, int lastIndex) {
		return new PathSelectionModelEvent(type, lastIndex);
	}
	
	public static PathSelectionModelEvent of(FileSelectionEventType type, String path, int index) {
		return new PathSelectionModelEvent(type, path, index);
	}
	
	protected PathSelectionModelEvent(FileSelectionEventType type, int lastIndex) {
		this(type, null, lastIndex);
	}

	protected PathSelectionModelEvent(FileSelectionEventType type, String path, int index) {
		requireNonNull(type);
		
		if (type.shouldHavePath() && path == null)
			throw new IllegalArgumentException("Event type requires non-null path value: " + type);
		
		if (index < 0)
			throw new IllegalArgumentException("Index should be greater or equals to 0");
		
		this.type = type;
		this.path = path;
		this.index = index;
	}
	
	public FileSelectionEventType getType() {
		return type;
	}

	public Optional<String> getPath() {
		return Optional.ofNullable(this.path);
	}
	
	public int getIndex() {
		return this.index;
	}
}
