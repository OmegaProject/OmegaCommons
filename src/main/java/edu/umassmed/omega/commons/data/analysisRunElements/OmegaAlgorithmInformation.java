/*******************************************************************************
 * Copyright (C) 2014 University of Massachusetts Medical School Alessandro
 * Rigano (Program in Molecular Medicine) Caterina Strambio De Castillia
 * (Program in Molecular Medicine)
 *
 * Created by the Open Microscopy Environment inteGrated Analysis (OMEGA) team:
 * Alex Rigano, Caterina Strambio De Castillia, Jasmine Clark, Vanni Galli,
 * Raffaello Giulietti, Loris Grossi, Eric Hunter, Tiziano Leidi, Jeremy Luban,
 * Ivo Sbalzarini and Mario Valle.
 *
 * Key contacts: Caterina Strambio De Castillia: caterina.strambio@umassmed.edu
 * Alex Rigano: alex.rigano@umassmed.edu
 *
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 *******************************************************************************/
package edu.umassmed.omega.commons.data.analysisRunElements;

import java.util.Date;

import edu.umassmed.omega.commons.data.coreElements.OmegaNamedElement;

public class OmegaAlgorithmInformation extends OmegaNamedElement {

	private static String DISPLAY_NAME = "Algorithm Information";

	private final String version;
	private final String description;
	private final Date publicationDate;
	private final String reference;
	private String authors;

	// public OmegaAlgorithmInformation(final String name, final double version,
	// final String description) {
	// super(-1L, name);
	//
	// this.author = null;
	// this.version = version;
	// this.description = description;
	// this.publicationDate = Calendar.getInstance().getTime();
	// }

	// public OmegaAlgorithmInformation(final String name, final double version,
	// final String description, final OmegaPerson author,String reference) {
	// this(name, version, description, reference);
	//
	// this.author = author;
	// }

	public OmegaAlgorithmInformation(final String name, final String version,
			final String description, final String authors,
			final Date publicationDate, final String reference) {
		super(-1L, name);

		this.version = version;
		this.description = description;

		this.authors = authors;

		this.publicationDate = publicationDate;

		this.reference = reference;
	}

	public String getDescription() {
		return this.description;
	}

	public String getVersion() {
		return this.version;
	}

	public Date getPublicationData() {
		return this.publicationDate;
	}

	public String getAuthors() {
		return this.authors;
	}

	public void changeAuthor(final String string) {
		this.authors = string;
	}

	public String getReference() {
		return this.reference;
	}

	public static String getStaticDisplayName() {
		return OmegaAlgorithmInformation.DISPLAY_NAME;
	}

	@Override
	public String getDynamicDisplayName() {
		return OmegaAlgorithmInformation.getStaticDisplayName();
	}
}
