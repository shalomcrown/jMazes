package jmazes;

import java.util.Arrays;

public class MazeCell {
	enum Walls {EAST, SOUTH, WEST, NORTH}

	boolean walls[] = new boolean[Walls.values().length];

	int column;
	int row;
	boolean visited = false;

	public MazeCell() {
	}

	public MazeCell(int column, int row) {
		super();
		this.column = column;
		this.row = row;
	}

	public void clear() {
		 walls = new boolean[]{true, true, true, true};
		 visited = false;
	}

	public boolean[] getWalls() {
		return walls;
	}

	public int getColumn() {
		return column;
	}

	public int getRow() {
		return row;
	}

	public void setWalls(boolean[] walls) {
		this.walls = walls;
	}

	public void setColumn(int column) {
		this.column = column;
	}

	public void setRow(int row) {
		this.row = row;
	}

	@Override
	public String toString() {
		return "MazeCell [walls=" + Arrays.toString(walls) + ", column=" + column + ", row=" + row + ", visited="
				+ visited + "]";
	}

	public boolean isVisited() {
		return visited;
	}

	public void setVisited(boolean visited) {
		this.visited = visited;
	}

}
