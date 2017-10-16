/*******************************************************************************
 * Copyright (C) 2014 University of Massachusetts Medical School Alessandro
 * Rigano (Program in Molecular Medicine) Caterina Strambio De Castillia
 * (Program in Molecular Medicine)
 *
 * Created by the Open Microscopy Environment inteGrated Analysis (OMEGA) team:
 * Alex Rigano, Caterina Strambio De Castillia, Jasmine Clark, Vanni Galli,
 * Raffaello Giulietti, Loris Grossi, Eric Hunter, Tiziano Leidi, Jeremy Luban,
 * Ivo ErrorIndex and Mario Valle.
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
import java.util.Map;

import edu.umassmed.omega.commons.data.coreElements.OmegaExperimenter;
import edu.umassmed.omega.commons.data.coreElements.OmegaPlane;
import edu.umassmed.omega.commons.data.trajectoryElements.OmegaROI;

public class OmegaSNRRun extends OmegaAnalysisRun {
	
	private static String DISPLAY_NAME = "SNR Estimation Run";

	private final Double resultingAvgCenterSignal;
	private final Double resultingAvgPeakSignal;
	private final Double resultingAvgMeanSignal;
	private final Double resultingBGR;
	private final Double resultingNoise;
	private final Double resultingAvgSNR;
	private final Double resultingMinSNR;
	private final Double resultingMaxSNR;
	private final Double resultingAvgErrorIndexSNR;
	private final Double resultingMinErrorIndexSNR;
	private final Double resultingMaxErrorIndexSNR;
	
	private final Map<OmegaPlane, Double> resultingImageAvgCenterSignal;
	private final Map<OmegaPlane, Double> resultingImageAvgPeakSignal;
	private final Map<OmegaPlane, Double> resultingImageAvgMeanSignal;
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
	private final Map<OmegaROI, Double> resultingLocalBackgrounds;
	private final Map<OmegaROI, Double> resultingLocalNoise;
	private final Map<OmegaROI, Double> resultingLocalSNR;
	private final Map<OmegaROI, Double> resultingLocalErrorIndexSNR;
	
	public OmegaSNRRun(final OmegaExperimenter owner,
			final OmegaRunDefinition algorithmSpec,
			final Map<OmegaPlane, Double> resultingImageAvgCenterSignal,
			final Map<OmegaPlane, Double> resultingImageAvgPeakSignal,
			final Map<OmegaPlane, Double> resultingImageAvgMeanSignal,
			final Map<OmegaPlane, Double> resultingImageBGR,
			final Map<OmegaPlane, Double> resultingImageNoise,
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
			final Map<OmegaROI, Double> resultingLocalBackgrounds,
			final Map<OmegaROI, Double> resultingLocalNoise,
			final Map<OmegaROI, Double> resultingLocalSNR,
			final Map<OmegaROI, Double> resultingLocalErrorIndexSNR,
			final Double resultingAvgCenterSignal,
			final Double resultingAvgPeakSignal,
			final Double resultingAvgMeanSignal, final Double resultingBGR,
			final Double resultingNoise, final Double resultingAvgSNR,
			final Double resultingMinSNR, final Double resultingMaxSNR,
			final Double resultingAvgErrorIndexSNR,
			final Double resultingMinErrorIndexSNR,
			final Double resultingMaxErrorIndexSNR) {
		super(owner, algorithmSpec, AnalysisRunType.OmegaSNRRun);
		
		this.resultingImageAvgCenterSignal = resultingImageAvgCenterSignal;
		this.resultingImageAvgPeakSignal = resultingImageAvgPeakSignal;
		this.resultingImageAvgMeanSignal = resultingImageAvgMeanSignal;
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
		this.resultingLocalBackgrounds = resultingLocalBackgrounds;
		this.resultingLocalNoise = resultingLocalNoise;
		this.resultingLocalSNR = resultingLocalSNR;
		this.resultingLocalErrorIndexSNR = resultingLocalErrorIndexSNR;
		this.resultingAvgCenterSignal = resultingAvgCenterSignal;
		this.resultingAvgPeakSignal = resultingAvgPeakSignal;
		this.resultingAvgMeanSignal = resultingAvgMeanSignal;
		this.resultingBGR = resultingBGR;
		this.resultingNoise = resultingNoise;
		this.resultingAvgSNR = resultingAvgSNR;
		this.resultingMinSNR = resultingMinSNR;
		this.resultingMaxSNR = resultingMaxSNR;
		this.resultingAvgErrorIndexSNR = resultingAvgErrorIndexSNR;
		this.resultingMinErrorIndexSNR = resultingMinErrorIndexSNR;
		this.resultingMaxErrorIndexSNR = resultingMaxErrorIndexSNR;
	}
	
	public OmegaSNRRun(final OmegaExperimenter owner,
			final OmegaRunDefinition algorithmSpec, final Date timeStamps,
			final String name,
			final Map<OmegaPlane, Double> resultingImageAvgCenterSignal,
			final Map<OmegaPlane, Double> resultingImageAvgPeakSignal,
			final Map<OmegaPlane, Double> resultingImageAvgMeanSignal,
			final Map<OmegaPlane, Double> resultingImageBGR,
			final Map<OmegaPlane, Double> resultingImageNoise,
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
			final Map<OmegaROI, Double> resultingLocalBackgrounds,
			final Map<OmegaROI, Double> resultingLocalNoise,
			final Map<OmegaROI, Double> resultingLocalSNR,
			final Map<OmegaROI, Double> resultingLocalErrorIndexSNR,
			final Double resultingAvgCenterSignal,
			final Double resultingAvgPeakSignal,
			final Double resultingAvgMeanSignal, final Double resultingBGR,
			final Double resultingNoise, final Double resultingAvgSNR,
			final Double resultingMinSNR, final Double resultingMaxSNR,
			final Double resultingAvgErrorIndexSNR,
			final Double resultingMinErrorIndexSNR,
			final Double resultingMaxErrorIndexSNR) {
		super(owner, algorithmSpec, AnalysisRunType.OmegaSNRRun, timeStamps,
				name);
		
		this.resultingImageAvgCenterSignal = resultingImageAvgCenterSignal;
		this.resultingImageAvgPeakSignal = resultingImageAvgPeakSignal;
		this.resultingImageAvgMeanSignal = resultingImageAvgMeanSignal;
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
		this.resultingLocalBackgrounds = resultingLocalBackgrounds;
		this.resultingLocalNoise = resultingLocalNoise;
		this.resultingLocalSNR = resultingLocalSNR;
		this.resultingLocalErrorIndexSNR = resultingLocalErrorIndexSNR;
		this.resultingAvgCenterSignal = resultingAvgCenterSignal;
		this.resultingAvgPeakSignal = resultingAvgPeakSignal;
		this.resultingAvgMeanSignal = resultingAvgMeanSignal;
		this.resultingBGR = resultingBGR;
		this.resultingNoise = resultingNoise;
		this.resultingAvgSNR = resultingAvgSNR;
		this.resultingMinSNR = resultingMinSNR;
		this.resultingMaxSNR = resultingMaxSNR;
		this.resultingAvgErrorIndexSNR = resultingAvgErrorIndexSNR;
		this.resultingMinErrorIndexSNR = resultingMinErrorIndexSNR;
		this.resultingMaxErrorIndexSNR = resultingMaxErrorIndexSNR;
	}

	public Double getResultingAverageCenterSignal() {
		return this.resultingAvgCenterSignal;
	}

	public Double getResultingAveragePeakSignal() {
		return this.resultingAvgPeakSignal;
	}

	public Double getResultingAverageMeanSignal() {
		return this.resultingAvgMeanSignal;
	}
	
	public Double getResultingBackground() {
		return this.resultingBGR;
	}

	public Double getResultingNoise() {
		return this.resultingNoise;
	}

	public Double getResultingAvgSNR() {
		return this.resultingAvgSNR;
	}

	public Double getResultingMaxSNR() {
		return this.resultingMaxSNR;
	}

	public Double getResultingMinSNR() {
		return this.resultingMinSNR;
	}

	public Double getResultingAvgErrorIndexSNR() {
		return this.resultingAvgErrorIndexSNR;
	}

	public Double getResultingMaxErrorIndexSNR() {
		return this.resultingMaxErrorIndexSNR;
	}

	public Double getResultingMinErrorIndexSNR() {
		return this.resultingMinErrorIndexSNR;
	}

	public Map<OmegaPlane, Double> getResultingImageAverageCenterSignal() {
		return this.resultingImageAvgCenterSignal;
	}

	public Map<OmegaPlane, Double> getResultingImageAveragePeakSignal() {
		return this.resultingImageAvgPeakSignal;
	}

	public Map<OmegaPlane, Double> getResultingImageAverageMeanSignal() {
		return this.resultingImageAvgMeanSignal;
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
	
	public Map<OmegaROI, Double> getResultingLocalBackgrounds() {
		return this.resultingLocalBackgrounds;
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

	public static String getStaticDisplayName() {
		return OmegaSNRRun.DISPLAY_NAME;
	}

	@Override
	public String getDynamicDisplayName() {
		return OmegaSNRRun.getStaticDisplayName();
	}
}
