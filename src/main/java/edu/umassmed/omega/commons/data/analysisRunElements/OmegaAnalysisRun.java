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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import edu.umassmed.omega.commons.constants.OmegaConstants;
import edu.umassmed.omega.commons.data.coreElements.OmegaExperimenter;
import edu.umassmed.omega.commons.data.coreElements.OmegaNamedElement;

public abstract class OmegaAnalysisRun extends OmegaNamedElement implements
		OmegaAnalysisRunContainerInterface {

	private static String DISPLAY_NAME = "Analysis Run";

	// private final String name;

	private final Date timeStamps;

	private OmegaExperimenter experimenter;

	// TODO aggiungere OmegaExperimenterGroup permissions

	private final OmegaRunDefinition algorithmSpec;

	private List<OmegaAnalysisRun> analysisRuns;

	private final AnalysisRunType type;

	public OmegaAnalysisRun(final OmegaExperimenter owner,
			final OmegaRunDefinition algorithmSpec, final AnalysisRunType type) {
		super(-1L, "");

		this.timeStamps = Calendar.getInstance().getTime();

		this.experimenter = owner;

		this.algorithmSpec = algorithmSpec;

		this.type = type;

		this.analysisRuns = new ArrayList<OmegaAnalysisRun>();

		final StringBuffer nameBuf = new StringBuffer();
		final DateFormat format = new SimpleDateFormat(
				OmegaConstants.OMEGA_DATE_FORMAT);
		nameBuf.append(format.format(this.timeStamps));
		if (algorithmSpec != null) {
			nameBuf.append("_");
			nameBuf.append(algorithmSpec.getAlgorithmInfo().getName());
		}
		// this.name = nameBuf.toString();
		this.setName(nameBuf.toString());
	}

	public OmegaAnalysisRun(final OmegaExperimenter owner,
			final OmegaRunDefinition algorithmSpec, final AnalysisRunType type,
			final String name) {
		super(-1L, "");

		this.timeStamps = Calendar.getInstance().getTime();

		this.experimenter = owner;

		this.algorithmSpec = algorithmSpec;

		this.type = type;

		this.analysisRuns = new ArrayList<OmegaAnalysisRun>();

		final StringBuffer nameBuf = new StringBuffer();
		final DateFormat format = new SimpleDateFormat(
				OmegaConstants.OMEGA_DATE_FORMAT);
		nameBuf.append(format.format(this.timeStamps));
		nameBuf.append("_");
		nameBuf.append(name);
		// this.name = nameBuf.toString();
		this.setName(nameBuf.toString());
	}

	public OmegaAnalysisRun(final OmegaExperimenter owner,
			final OmegaRunDefinition algorithmSpec, final AnalysisRunType type,
			final Date timeStamps, final String name) {
		super(-1L, name);

		this.timeStamps = timeStamps;

		this.experimenter = owner;

		this.algorithmSpec = algorithmSpec;

		this.type = type;

		this.analysisRuns = new ArrayList<OmegaAnalysisRun>();

		// this.name = name;
	}

	public OmegaAnalysisRun(final OmegaExperimenter owner,
			final OmegaRunDefinition algorithmSpec, final AnalysisRunType type,
			final List<OmegaAnalysisRun> analysisRuns) {
		this(owner, algorithmSpec, type);

		this.analysisRuns = analysisRuns;
	}

	public AnalysisRunType getType() {
		return this.type;
	}

	public Date getTimeStamps() {
		return this.timeStamps;
	}

	public OmegaExperimenter getExperimenter() {
		return this.experimenter;
	}

	public OmegaRunDefinition getAlgorithmSpec() {
		return this.algorithmSpec;
	}

	@Override
	public List<OmegaAnalysisRun> getAnalysisRuns() {
		return this.analysisRuns;
	}

	@Override
	public void addAnalysisRun(final OmegaAnalysisRun analysisRun) {
		this.analysisRuns.add(analysisRun);
	}

	@Override
	public void removeAnalysisRun(final OmegaAnalysisRun analysisRun) {
		this.analysisRuns.remove(analysisRun);
	}

	@Override
	public boolean containsAnalysisRun(final long id) {
		for (final OmegaAnalysisRun analysisRun : this.analysisRuns) {
			if (analysisRun.getElementID() == id)
				return true;
		}
		return false;
	}

	public void changeExperimenter(final OmegaExperimenter experimenter) {
		this.experimenter = experimenter;
	}

	public static String getStaticDisplayName() {
		return OmegaAnalysisRun.DISPLAY_NAME;
	}

	@Override
	public String getDynamicDisplayName() {
		return OmegaAnalysisRun.getStaticDisplayName();
	}
}
