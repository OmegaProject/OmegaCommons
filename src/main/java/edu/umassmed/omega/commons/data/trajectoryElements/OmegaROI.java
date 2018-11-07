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
package edu.umassmed.omega.commons.data.trajectoryElements;

import edu.umassmed.omega.commons.data.coreElements.OmegaElement;

public class OmegaROI extends OmegaElement {

	private static String DISPLAY_NAME = "Region Of Interest";
	
	private int frameIndex;
	private final double x, y;
	private double realX, realY;

	public OmegaROI(final int frameIndex, final double x, final double y) {
		super(-1L);

		this.frameIndex = frameIndex;
		this.x = x;
		this.y = y;
		this.realX = x;
		this.realY = y;
	}

	public OmegaROI(final int frameIndex, final double x, final double y,
			final double realX, final double realY) {
		super(-1L);

		this.frameIndex = frameIndex;
		this.x = x;
		this.y = y;
		this.realX = realX;
		this.realY = realY;
	}

	public int getFrameIndex() {
		return this.frameIndex;
	}

	public void setFrameIndex(final int frameIndex) {
		this.frameIndex = frameIndex;
	}

	public double getX() {
		return this.x;
	}

	public double getY() {
		return this.y;
	}
	
	public double getRealX() {
		return this.realX;
	}
	
	public void setRealX(final Double realX) {
		this.realX = realX;
	}

	public double getRealY() {
		return this.realY;
	}
	
	public void setRealY(final Double realY) {
		this.realY = realY;
	}
	
	public static String getStaticDisplayName() {
		return OmegaROI.DISPLAY_NAME;
	}

	@Override
	public String getDynamicDisplayName() {
		return OmegaROI.getStaticDisplayName();
	}
}
