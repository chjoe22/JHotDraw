/*
 * @(#)ToggleGridCommand.java
 *
 * Project:		JHotdraw - a GUI framework for technical drawings
 *				http://www.jhotdraw.org
 *				http://jhotdraw.sourceforge.net
 * Copyright:	� by the original author(s) and all contributors
 * License:		Lesser GNU Public License (LGPL)
 *				http://www.opensource.org/licenses/lgpl-license.html
 */

package CH.ifa.draw.standard;

import java.util.*;
import java.awt.Point;
import CH.ifa.draw.util.Command;
import CH.ifa.draw.framework.*;

/**
 * A command to toggle the snap to grid behavior.
 *
 * @version <$CURRENT_VERSION$>
 */
public class ToggleGridCommand extends AbstractCommand {

	private Point fGrid;

   /**
	* Constructs a toggle grid command.
	* @param name the command name
	* @param image the pathname of the image
	* @param grid the grid size. A grid size of 1,1 turns grid snapping off.
	*/
	public ToggleGridCommand(String name, DrawingView view, Point grid) {
		super(name, view);
		fGrid = new Point(grid.x, grid.y);
	}

	public void execute() {
		PointConstrainer grid = view().getConstrainer();
		if (grid != null) {
			view().setConstrainer(null);
		}
		else {
			view().setConstrainer(new GridConstrainer(fGrid.x, fGrid.y));
		}
	}
}


