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
package edu.umassmed.omega.commons.data.coreElements;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import edu.umassmed.omega.commons.data.analysisRunElements.OmegaAnalysisRun;
import edu.umassmed.omega.commons.data.analysisRunElements.OmegaAnalysisRunContainer;

public class OmegaImagePixels extends OmegaElement implements
        OmegaAnalysisRunContainer {

	private OmegaImage image;

	private final String pixelsType;

	private final int sizeX, sizeY, sizeZ, sizeC, sizeT;

	private double physicalSizeX, physicalSizeY, physicalSizeZ, physicalSizeT;

	private int selectedZ;

	private final boolean[] selectedC;

	private final Map<Integer, Map<Integer, List<OmegaFrame>>> frames;

	private final List<OmegaAnalysisRun> analysisRuns;

	public OmegaImagePixels(final Long elementID, final String pixelsType,
	        final int sizeX, final int sizeY, final int sizeZ, final int sizeC,
	        final int sizeT, final double pixelSizeX, final double pixelSizeY,
	        final double pixelSizeZ) {
		super(elementID);

		this.image = null;

		this.pixelsType = pixelsType;

		this.sizeX = sizeX;
		this.sizeY = sizeY;
		this.sizeZ = sizeZ;

		this.sizeC = sizeC;
		this.selectedC = new boolean[sizeC];
		this.sizeT = sizeT;

		if (pixelSizeX == 0) {
			this.physicalSizeX = -1;
		} else {
			this.physicalSizeX = pixelSizeX;
		}
		if (pixelSizeY == 0) {
			this.physicalSizeY = -1;
		} else {
			this.physicalSizeY = pixelSizeY;
		}
		if (pixelSizeZ == 0) {
			this.physicalSizeZ = -1;
		} else {
			this.physicalSizeZ = pixelSizeZ;
		}

		this.selectedZ = -1;
		for (int i = 0; i < sizeC; i++) {
			this.selectedC[i] = false;
		}

		this.frames = new LinkedHashMap<>();
		this.analysisRuns = new ArrayList<>();
	}

	public void setParentImage(final OmegaImage image) {
		this.image = image;
	}

	public OmegaImage getParentImage() {
		return this.image;
	}

	public String getPixelsType() {
		return this.pixelsType;
	}

	public int getSizeX() {
		return this.sizeX;
	}

	public int getSizeY() {
		return this.sizeY;
	}

	public int getSizeZ() {
		return this.sizeZ;
	}

	public int getSizeC() {
		return this.sizeC;
	}

	public int getSizeT() {
		return this.sizeT;
	}

	public double getPhysicalSizeX() {
		return this.physicalSizeX;
	}

	public void setPhysicalSizeX(final double sizeX) {
		this.physicalSizeX = sizeX;
	}

	public double getPhysicalSizeY() {
		return this.physicalSizeY;
	}

	public void setPhysicalSizeY(final double sizeY) {
		this.physicalSizeY = sizeY;
	}

	public double getPhysicalSizeZ() {
		return this.physicalSizeZ;
	}

	public void setPhysicalSizeZ(final double sizeZ) {
		this.physicalSizeZ = sizeZ;
	}

	public double getPhysicalSizeT() {
		return this.physicalSizeT;
	}

	public void setPhysicalSizeT(final double sizeT) {
		this.physicalSizeT = sizeT;
	}

	public List<OmegaFrame> getFrames(final Integer c, final Integer z) {
		List<OmegaFrame> frameList = null;
		if (this.frames.containsKey(c)) {
			final Map<Integer, List<OmegaFrame>> subMap = this.frames.get(c);
			if (subMap.containsKey(z)) {
				frameList = subMap.get(z);
			} else {
				frameList = new ArrayList<>();
			}
		} else {
			frameList = new ArrayList<>();
		}
		return frameList;
	}

	public void addFrames(final Integer c, final Integer z,
	        final List<OmegaFrame> frames) {
		List<OmegaFrame> frameList = null;
		Map<Integer, List<OmegaFrame>> subMap = null;
		if (this.frames.containsKey(c)) {
			subMap = this.frames.get(c);
			if (subMap.containsKey(z)) {
				frameList = subMap.get(z);
			} else {
				frameList = new ArrayList<>();
			}
		} else {
			subMap = new LinkedHashMap<>();
			frameList = new ArrayList<>();
		}

		frameList.addAll(frames);
		subMap.put(z, frameList);
		this.frames.put(c, subMap);
	}

	public void addFrame(final Integer c, final Integer z,
	        final OmegaFrame frame) {
		List<OmegaFrame> frameList = null;
		Map<Integer, List<OmegaFrame>> subMap = null;
		if (this.frames.containsKey(c)) {
			subMap = this.frames.get(c);
			if (subMap.containsKey(z)) {
				frameList = subMap.get(z);
			} else {
				frameList = new ArrayList<>();
			}
		} else {
			subMap = new LinkedHashMap<>();
			frameList = new ArrayList<>();
		}

		frameList.add(frame);
		subMap.put(z, frameList);
		this.frames.put(c, subMap);
	}

	public OmegaFrame getFrame(final Integer c, final Integer z, final long id) {
		List<OmegaFrame> frameList = null;
		if (this.frames.containsKey(c)) {
			final Map<Integer, List<OmegaFrame>> subMap = this.frames.get(c);
			if (subMap.containsKey(z)) {
				frameList = subMap.get(z);
			} else {
				frameList = new ArrayList<>();
			}
		} else {
			frameList = new ArrayList<>();
		}

		for (final OmegaFrame frame : frameList) {
			if (frame.getElementID() == id)
				return frame;
		}
		return null;
	}

	public boolean containsFrame(final Integer c, final Integer z, final long id) {
		List<OmegaFrame> frameList = null;
		if (this.frames.containsKey(c)) {
			final Map<Integer, List<OmegaFrame>> subMap = this.frames.get(c);
			if (subMap.containsKey(z)) {
				frameList = subMap.get(z);
			} else {
				frameList = new ArrayList<>();
			}
		} else {
			frameList = new ArrayList<>();
		}

		for (final OmegaFrame frame : frameList) {
			if (frame.getElementID() == id)
				return true;
		}
		return false;
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

	public int getSelectedZ() {
		return this.selectedZ;
	}

	public void setSelectedZ(final int newZ) {
		this.selectedZ = newZ;
	}

	public boolean[] getSelectedC() {
		return this.selectedC;
	}

	public void setSelectedC(final int index, final boolean isActive) {
		this.selectedC[index] = isActive;
	}
}
