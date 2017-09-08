/*
 * #%L
 * S2P Core
 * %%
 * Copyright (C) 2016 - 2017 José Luis Capelo Martínez, José Eduardo Araújo, Florentino Fdez-Riverola, Miguel
 * 			Reboiro-Jato, Hugo López-Fernández, and Daniel Glez-Peña
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */
package org.sing_group.seda.matcher;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;

import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.TypeSafeDiagnosingMatcher;

public class HasEqualFileContentMatcher extends TypeSafeDiagnosingMatcher<File> {
	private File expectedFile;

	public HasEqualFileContentMatcher(File expectedFile) {
		this.expectedFile = expectedFile;
	}

	@Override
	public void describeTo(Description description) {
		description.appendText("File: ").appendText(this.expectedFile.getAbsolutePath());
	}

	@Override
	protected boolean matchesSafely(File item, Description mismatchDescription) {
		final byte[] expectedContent, actualContent;

		try {
			expectedContent = Files.readAllBytes(this.expectedFile.toPath());
		} catch (IOException e) {
			mismatchDescription.appendText("Exception thrown while reading file: ")
				.appendText(this.expectedFile.getAbsolutePath());

			return false;
		}
		try {
			actualContent = Files.readAllBytes(item.toPath());
		} catch (IOException e) {
			mismatchDescription.appendText("Exception thrown while reading file: ")
				.appendText(item.getAbsolutePath());

			return false;
		}

		if (Arrays.equals(expectedContent, actualContent)) {
			return true;
		} else {
			mismatchDescription
				.appendText(item.getAbsolutePath())
				.appendText(" does not have the same content");

			return false;
		}
	}

	@Factory
	public static HasEqualFileContentMatcher hasEqualFileContent(File expectedFile) {
		return new HasEqualFileContentMatcher(expectedFile);
	}
}
