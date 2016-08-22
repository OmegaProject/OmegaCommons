/*******************************************************************************
 * Copyright (C) 2014 University of Massachusetts Medical School
 * Alessandro Rigano (Program in Molecular Medicine)
 * Caterina Strambio De Castillia (Program in Molecular Medicine)
 *
 * Created by the Open Microscopy Environment inteGrated Analysis (OMEGA) team:
 * Alex Rigano, Caterina Strambio De Castillia, Jasmine Clark, Vanni Galli,
 * Raffaello Giulietti, Loris Grossi, Eric Hunter, Tiziano Leidi, Jeremy Luban,
 * Ivo ErrorIndex and Mario Valle.
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
import edu.umassmed.omega.commons.data.coreElements.OmegaPlane;
import edu.umassmed.omega.commons.data.trajectoryElements.OmegaROI;

public class OmegaSNRRun extends OmegaAnalysisRun {

	private final Map<OmegaPlane, Double> resultingImageBGR;
	private final Map<OmegaPlane, Double> resultingImageNoise;
	private final Map<OmegaPlane, Double> resultingImageAvgSNR;
	private final Map<OmegaPlane, Double> resultingImageMinSNR;
	private final Map<OmegaPlane, Double> resultingImageMaxSNR;
	private final Map<OmegaPlane, Double> resultingImageAvgErrorIndexSNR;
	private final Map<OmegaPlane, Double> resultingImageMinErrorIndexSNR;
	private final Map<OmegaPlane, Double> resultingImageMaxErrorIndexSNR;
	// VA SU PARTICLE
	private final Map<OmegaROI, Integer> resultingLocalCenterSignal;
	private final Map<OmegaROI, Double> resultingLocalMeanSignal;
	private final Map<OmegaROI, Integer> resultingLocalParticleArea;
	// VA SU PARTICLE
	private final Map<OmegaROI, Integer> resultingLocalPeakSignal;
	private final Map<OmegaROI, Double> resultingLocalNoise;
	private final Map<OmegaROI, Double> resultingLocalSNR;
	private final Map<OmegaROI, Double> resultingLocalErrorIndexSNR;

	public OmegaSNRRun(final OmegaExperimenter owner,
			final OmegaRunDefinition algorithmSpec,
			final Map<OmegaPlane, Double> resultingImageNoise,
			final Map<OmegaPlane, Double> resultingImageBGR,
			final Map<OmegaPlane, Double> resultingImageAverageSNR,
			final Map<OmegaPlane, Double> resultingImageMinimumSNR,
			final Map<OmegaPlane, Double> resultingImageMaximumSNR,
			final Map<OmegaPlane, Double> resultingImageAverageErrorIndexSNR,
			final Map<OmegaPlane, Double> resultingImageMinimumErrorIndexSNR,
			final Map<OmegaPlane, Double> resultingImageMaximumErrorIndexSNR,
			final Map<OmegaROI, Integer> resultingLocalCenterSignal,
			final Map<OmegaROI, Double> resultingLocalMeanSignal,
			final Map<OmegaROI, Integer> resultingLocalParticleArea,
			final Map<OmegaROI, Integer> resultingLocalPeakSignal,
			final Map<OmegaROI, Double> resultingLocalNoise,
			final Map<OmegaROI, Double> resultingLocalSNR,
	        final Map<OmegaROI, Double> resultingLocalErrorIndexSNR) {
		super(owner, algorithmSpec, AnalysisRunType.OmegaSNRRun);

		this.resultingImageBGR = resultingImageBGR;
		this.resultingImageNoise = resultingImageNoise;
		this.resultingImageAvgSNR = resultingImageAverageSNR;
		this.resultingImageMinSNR = resultingImageMinimumSNR;
		this.resultingImageMaxSNR = resultingImageMaximumSNR;
		this.resultingImageAvgErrorIndexSNR = resultingImageAverageErrorIndexSNR;
		this.resultingImageMinErrorIndexSNR = resultingImageMinimumErrorIndexSNR;
		this.resultingImageMaxErrorIndexSNR = resultingImageMaximumErrorIndexSNR;
		this.resultingLocalCenterSignal = resultingLocalCenterSignal;
		this.resultingLocalMeanSignal = resultingLocalMeanSignal;
		this.resultingLocalParticleArea = resultingLocalParticleArea;
		this.resultingLocalPeakSignal = resultingLocalPeakSignal;
		this.resultingLocalNoise = resultingLocalNoise;
		this.resultingLocalSNR = resultingLocalSNR;
		this.resultingLocalErrorIndexSNR = resultingLocalErrorIndexSNR;
	}

	public OmegaSNRRun(final OmegaExperimenter owner,
			final OmegaRunDefinition algorithmSpec, final Date timeStamps,
	        final String name,
			final Map<OmegaPlane, Double> resultingImageNoise,
			final Map<OmegaPlane, Double> resultingImageBGR,
			final Map<OmegaPlane, Double> resultingImageAverageSNR,
			final Map<OmegaPlane, Double> resultingImageMinimumSNR,
			final Map<OmegaPlane, Double> resultingImageMaximumSNR,
			final Map<OmegaPlane, Double> resultingImageAverageErrorIndexSNR,
			final Map<OmegaPlane, Double> resultingImageMinimumErrorIndexSNR,
			final Map<OmegaPlane, Double> resultingImageMaximumErrorIndexSNR,
			final Map<OmegaROI, Integer> resultingLocalCenterSignal,
			final Map<OmegaROI, Double> resultingLocalMeanSignal,
			final Map<OmegaROI, Integer> resultingLocalParticleArea,
			final Map<OmegaROI, Integer> resultingLocalPeakSignal,
			final Map<OmegaROI, Double> resultingLocalNoise,
			final Map<OmegaROI, Double> resultingLocalSNR,
	        final Map<OmegaROI, Double> resultingLocalErrorIndexSNR) {
		super(owner, algorithmSpec, AnalysisRunType.OmegaSNRRun, timeStamps,
				name);

		this.resultingImageBGR = resultingImageBGR;
		this.resultingImageNoise = resultingImageNoise;
		this.resultingImageAvgSNR = resultingImageAverageSNR;
		this.resultingImageMinSNR = resultingImageMinimumSNR;
		this.resultingImageMaxSNR = resultingImageMaximumSNR;
		this.resultingImageAvgErrorIndexSNR = resultingImageAverageErrorIndexSNR;
		this.resultingImageMinErrorIndexSNR = resultingImageMinimumErrorIndexSNR;
		this.resultingImageMaxErrorIndexSNR = resultingImageMaximumErrorIndexSNR;
		this.resultingLocalCenterSignal = resultingLocalCenterSignal;
		this.resultingLocalMeanSignal = resultingLocalMeanSignal;
		this.resultingLocalParticleArea = resultingLocalParticleArea;
		this.resultingLocalPeakSignal = resultingLocalPeakSignal;
		this.resultingLocalNoise = resultingLocalNoise;
		this.resultingLocalSNR = resultingLocalSNR;
		this.resultingLocalErrorIndexSNR = resultingLocalErrorIndexSNR;
	}

	public Map<OmegaPlane, Double> getResultingImageBGR() {
		return this.resultingImageBGR;
	}

	public Map<OmegaPlane, Double> getResultingImageNoise() {
		return this.resultingImageNoise;
	}

	public Map<OmegaPlane, Double> getResultingImageAverageSNR() {
		return this.resultingImageAvgSNR;
	}

	public Map<OmegaPlane, Double> getResultingImageMinimumSNR() {
		return this.resultingImageMinSNR;
	}

	public Map<OmegaPlane, Double> getResultingImageMaximumSNR() {
		return this.resultingImageMaxSNR;
	}

	public Map<OmegaPlane, Double> getResultingImageAverageErrorIndexSNR() {
		return this.resultingImageAvgErrorIndexSNR;
	}

	public Map<OmegaPlane, Double> getResultingImageMinimumErrorIndexSNR() {
		return this.resultingImageMinErrorIndexSNR;
	}

	public Map<OmegaPlane, Double> getResultingImageMaximumErrorIndexSNR() {
		return this.resultingImageMaxErrorIndexSNR;
	}

	public Map<OmegaROI, Integer> getResultingLocalCenterSignals() {
		return this.resultingLocalCenterSignal;
	}

	public Map<OmegaROI, Double> getResultingLocalMeanSignals() {
		return this.resultingLocalMeanSignal;
	}

	public Map<OmegaROI, Integer> getResultingLocalParticleArea() {
		return this.resultingLocalParticleArea;
	}

	public Map<OmegaROI, Integer> getResultingLocalPeakSignals() {
		return this.resultingLocalPeakSignal;
	}

	public Map<OmegaROI, Double> getResultingLocalNoises() {
		return this.resultingLocalNoise;
	}

	public Map<OmegaROI, Double> getResultingLocalSNRs() {
		return this.resultingLocalSNR;
	}

	public Map<OmegaROI, Double> getResultingLocalErrorIndexSNRs() {
		return this.resultingLocalErrorIndexSNR;
	}
}
