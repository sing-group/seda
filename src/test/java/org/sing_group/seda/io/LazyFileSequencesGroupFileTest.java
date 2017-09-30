package org.sing_group.seda.io;

import static org.sing_group.seda.io.TestFnaFileInformation.testFnaPath;

public class LazyFileSequencesGroupFileTest extends AbstractLazyFileSequencesGroupTest {
	@Override
	protected LazyFileSequencesGroup buildSequenceGroup() {
	  return new LazyFileSequencesGroup(testFnaPath());
	}
}
