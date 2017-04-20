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
package edu.umassmed.omega.commons.data.coreElements;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import edu.umassmed.omega.commons.data.analysisRunElements.OmegaAnalysisRun;
import edu.umassmed.omega.commons.data.analysisRunElements.OmegaAnalysisRunContainer;

public class OmegaImagePixels extends OmegaElement implements OmeroElement,
		OmegaAnalysisRunContainer {
	
	private OmegaImage image;
	
	private final String pixelsType;
	
	private final int sizeX, sizeY, sizeZ, sizeC, sizeT;
	
	private double physicalSizeX, physicalSizeY, physicalSizeZ, physicalSizeT;
	
	private int selectedZ, selectedT;
	private final boolean[] selectedC;
	
	private final Map<Integer, String> channelNames;
	
	private final Map<Integer, Map<Integer, List<OmegaPlane>>> frames;
	
	private final List<OmegaAnalysisRun> analysisRuns;
	
	private Long omeroId;
	
	public OmegaImagePixels(final String pixelsType, final int sizeX,
			final int sizeY, final int sizeZ, final int sizeC, final int sizeT,
			final double pixelSizeX, final double pixelSizeY,
			final double pixelSizeZ, final Map<Integer, String> channelNames) {
		super(-1L);
		this.omeroId = -1L;
		this.image = null;
		
		this.pixelsType = pixelsType;
		
		this.sizeX = sizeX;
		this.sizeY = sizeY;
		this.sizeZ = sizeZ;
		
		this.sizeC = sizeC;
		this.selectedC = new boolean[sizeC];
		this.channelNames = channelNames;
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
		this.physicalSizeT = -1;
		
		this.selectedZ = -1;
		this.selectedT = -1;
		for (int i = 0; i < sizeC; i++) {
			this.selectedC[i] = true;
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
	
	public Map<Integer, Map<Integer, List<OmegaPlane>>> getAllFrames() {
		return this.frames;
	}
	
	public List<OmegaPlane> getFrames(final Integer c, final Integer z) {
		List<OmegaPlane> frameList = null;
		if (this.frames.containsKey(c)) {
			final Map<Integer, List<OmegaPlane>> subMap = this.frames.get(c);
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
			final List<OmegaPlane> frames) {
		List<OmegaPlane> frameList = null;
		Map<Integer, List<OmegaPlane>> subMap = null;
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
			final OmegaPlane frame) {
		List<OmegaPlane> frameList = null;
		Map<Integer, List<OmegaPlane>> subMap = null;
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
	
	public OmegaPlane getFrame(final Integer c, final Integer z, final long id) {
		List<OmegaPlane> frameList = null;
		if (this.frames.containsKey(c)) {
			final Map<Integer, List<OmegaPlane>> subMap = this.frames.get(c);
			if (subMap.containsKey(z)) {
				frameList = subMap.get(z);
			} else {
				frameList = new ArrayList<>();
			}
		} else {
			frameList = new ArrayList<>();
		}
		
		for (final OmegaPlane frame : frameList) {
			if (frame.getElementID() == id)
				return frame;
		}
		return null;
	}
	
	public boolean containsFrame(final Integer c, final Integer z, final long id) {
		List<OmegaPlane> frameList = null;
		if (this.frames.containsKey(c)) {
			final Map<Integer, List<OmegaPlane>> subMap = this.frames.get(c);
			if (subMap.containsKey(z)) {
				frameList = subMap.get(z);
			} else {
				frameList = new ArrayList<>();
			}
		} else {
			frameList = new ArrayList<>();
		}
		
		for (final OmegaPlane frame : frameList) {
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
	
	public int getSelectedT() {
		return this.selectedT;
	}
	
	public void setSelectedT(final int newT) {
		this.selectedT = newT;
	}
	
	@Override
	public void setOmeroId(final Long omeroId) {
		this.omeroId = omeroId;
	}
	
	@Override
	public Long getOmeroId() {
		return this.omeroId;
	}
	
	public Map<Integer, String> getChannelNames() {
		return this.channelNames;
	}
}
