/*******************************************************************************
 * Copyright (C) 2014 University of Massachusetts Medical School
 * Alessandro Rigano (Program in Molecular Medicine)
 * Caterina Strambio De Castillia (Program in Molecular Medicine)
 *
 * Created by the Open Microscopy Environment inteGrated Analysis (OMEGA) team:
 * Alex Rigano, Caterina Strambio De Castillia, Jasmine Clark, Vanni Galli,
 * Raffaello Giulietti, Loris Grossi, Eric Hunter, Tiziano Leidi, Jeremy Luban,
 * Ivo Sbalzarini and Mario Valle.
 *
 * Key contacts:
 * Caterina Strambio De Castillia: caterina.strambio@umassmed.edu
 * Alex Rigano: alex.rigano@umassmed.edu
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *******************************************************************************/
package edu.umassmed.omega.commons.data.analysisRunElements;

import java.util.Date;
import java.util.Map;

import edu.umassmed.omega.commons.data.coreElements.OmegaExperimenter;
import edu.umassmed.omega.commons.data.coreElements.OmegaFrame;
import edu.umassmed.omega.commons.data.trajectoryElements.OmegaROI;

public class OmegaSNRRun extends OmegaAnalysisRun {

	private final Map<OmegaFrame, Double> resultingImageBGR;
	private final Map<OmegaFrame, Double> resultingImageNoise;
	private final Map<OmegaFrame, Double> resultingImageAvgSNR;
	private final Map<OmegaFrame, Double> resultingImageMinSNR;
	private final Map<OmegaFrame, Double> resultingImageMaxSNR;
	private final Map<OmegaROI, Integer> resultingLocalCenterSignals;
	private final Map<OmegaROI, Double> resultingLocalMeanSignals;
	private final Map<OmegaROI, Integer> resultingLocalSignalSizes;
	private final Map<OmegaROI, Integer> resultingLocalPeakSignals;
	private final Map<OmegaROI, Double> resultingLocalNoises;
	private final Map<OmegaROI, Double> resultingLocalSNRs;
	private final Map<OmegaROI, Double> resultingLocalBhattacharyyaPoissonSNRs;

	public OmegaSNRRun(final OmegaExperimenter owner,
	        final OmegaAlgorithmSpecification algorithmSpec,
	        final Map<OmegaFrame, Double> resultingImageNoise,
	        final Map<OmegaFrame, Double> resultingImageBGR,
	        final Map<OmegaFrame, Double> resultingImageAverageSNR,
	        final Map<OmegaFrame, Double> resultingImageMinimumSNR,
	        final Map<OmegaFrame, Double> resultingImageMaximumSNR,
	        final Map<OmegaROI, Integer> resultingLocalCenterSignals,
	        final Map<OmegaROI, Double> resultingLocalMeanSignals,
	        final Map<OmegaROI, Integer> resultingLocalSignalSizes,
	        final Map<OmegaROI, Integer> resultingLocalPeakSignals,
	        final Map<OmegaROI, Double> resultingLocalNoises,
	        final Map<OmegaROI, Double> resultingLocalSNRs,
			final Map<OmegaROI, Double> resultingLocalBhattacharyyaPoissonSNRs) {
		super(owner, algorithmSpec);

		this.resultingImageBGR = resultingImageBGR;
		this.resultingImageNoise = resultingImageNoise;
		this.resultingImageAvgSNR = resultingImageAverageSNR;
		this.resultingImageMinSNR = resultingImageMinimumSNR;
		this.resultingImageMaxSNR = resultingImageMaximumSNR;
		this.resultingLocalCenterSignals = resultingLocalCenterSignals;
		this.resultingLocalMeanSignals = resultingLocalMeanSignals;
		this.resultingLocalSignalSizes = resultingLocalSignalSizes;
		this.resultingLocalPeakSignals = resultingLocalPeakSignals;
		this.resultingLocalNoises = resultingLocalNoises;
		this.resultingLocalSNRs = resultingLocalSNRs;
		this.resultingLocalBhattacharyyaPoissonSNRs = resultingLocalBhattacharyyaPoissonSNRs;
	}

	public OmegaSNRRun(final OmegaExperimenter owner,
	        final OmegaAlgorithmSpecification algorithmSpec,
	        final Date timeStamps, final String name,
	        final Map<OmegaFrame, Double> resultingImageNoise,
	        final Map<OmegaFrame, Double> resultingImageBGR,
	        final Map<OmegaFrame, Double> resultingImageAverageSNR,
	        final Map<OmegaFrame, Double> resultingImageMinimumSNR,
	        final Map<OmegaFrame, Double> resultingImageMaximumSNR,
	        final Map<OmegaROI, Integer> resultingLocalCenterSignals,
	        final Map<OmegaROI, Double> resultingLocalMeanSignals,
	        final Map<OmegaROI, Integer> resultingLocalSignalSizes,
	        final Map<OmegaROI, Integer> resultingLocalPeakSignals,
	        final Map<OmegaROI, Double> resultingLocalNoises,
	        final Map<OmegaROI, Double> resultingLocalSNRs,
			final Map<OmegaROI, Double> resultingLocalBhattacharyyaPoissonSNRs) {
		super(owner, algorithmSpec, timeStamps, name);

		this.resultingImageBGR = resultingImageBGR;
		this.resultingImageNoise = resultingImageNoise;
		this.resultingImageAvgSNR = resultingImageAverageSNR;
		this.resultingImageMinSNR = resultingImageMinimumSNR;
		this.resultingImageMaxSNR = resultingImageMaximumSNR;
		this.resultingLocalCenterSignals = resultingLocalCenterSignals;
		this.resultingLocalMeanSignals = resultingLocalMeanSignals;
		this.resultingLocalSignalSizes = resultingLocalSignalSizes;
		this.resultingLocalPeakSignals = resultingLocalPeakSignals;
		this.resultingLocalNoises = resultingLocalNoises;
		this.resultingLocalSNRs = resultingLocalSNRs;
		this.resultingLocalBhattacharyyaPoissonSNRs = resultingLocalBhattacharyyaPoissonSNRs;
	}

	public Map<OmegaFrame, Double> getResultingImageBGR() {
		return this.resultingImageBGR;
	}

	public Map<OmegaFrame, Double> getResultingImageNoise() {
		return this.resultingImageNoise;
	}

	public Map<OmegaFrame, Double> getResultingImageAverageSNR() {
		return this.resultingImageAvgSNR;
	}

	public Map<OmegaFrame, Double> getResultingImageMinimumSNR() {
		return this.resultingImageMinSNR;
	}

	public Map<OmegaFrame, Double> getResultingImageMaximumSNR() {
		return this.resultingImageMaxSNR;
	}

	public Map<OmegaROI, Integer> getResultingLocalCenterSignals() {
		return this.resultingLocalCenterSignals;
	}

	public Map<OmegaROI, Double> getResultingLocalMeanSignals() {
		return this.resultingLocalMeanSignals;
	}

	public Map<OmegaROI, Integer> getResultingLocalSignalSizes() {
		return this.resultingLocalSignalSizes;
	}

	public Map<OmegaROI, Integer> getResultingLocalPeakSignals() {
		return this.resultingLocalPeakSignals;
	}

	public Map<OmegaROI, Double> getResultingLocalNoises() {
		return this.resultingLocalNoises;
	}

	public Map<OmegaROI, Double> getResultingLocalSNRs() {
		return this.resultingLocalSNRs;
	}

	public Map<OmegaROI, Double> getResultingLocalBhattacharyyaPoissonSNRs() {
		return this.resultingLocalBhattacharyyaPoissonSNRs;
	}
}
