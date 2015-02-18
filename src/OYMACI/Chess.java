package OYMACI;

public class Chess
{
	private int[][] board = new int[][] { { 4, 3, 2, 5, 6, 2, 3, 4 }, { 1, 1, 1, 1, 1, 1, 1, 1 },
			{ 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0 },
			{ 0, 0, 0, 0, 0, 0, 0, 0 }, { -1, -1, -1, -1, -1, -1, -1, -1 }, { -4, -3, -2, -5, -6, -2, -3, -4 }, // white
	};
	private int[][] copiedBoard = new int[8][8];
	private int space = 0;
	private int pawn = 1;
	private int bishop = 2; // fil
	private int knight = 3;	
	private int rook = 4; // kale
	private int queen = 5;
	private int king = 6;

	int getBoard(int x, int y)
	{
		if (x < 0 || x >= 8 || y < 0 || y >= 8)
			throw new ArrayIndexOutOfBoundsException();
		return board[x][y];
	}

	// postcondition: all of integer must be between 0 and 7
	String play(String color, int sourceX, int sourceY, int aimX, int aimY) // x is row, y is column
	{
		if (aimX < 0 || aimX >= 8 || aimY < 0 || aimY >= 8)
		{
			return "Error! Invalid aim.";
		}
		if (color.contentEquals("White"))
		{
			sourceX = 7 - sourceX;
			aimX = 7 - aimX;
			reverseBoard();
		}
		// chosen wrong stone
		if ((color.contentEquals("Black") && board[aimX][aimY] > 0)
				|| (color.contentEquals("White") && board[aimX][aimY] < 0))
		{
			return "Error! \nThere are \nyour stone \nin aim.";
		}
		// wrong stone
		if ((color.contentEquals("Black") && board[sourceX][sourceY] < 0)
				|| (color.contentEquals("White") && board[sourceX][sourceY] > 0))
		{
			return "Error! \nSource coordinate\n does not belong\n to you.";
		}
		// find kind of stone
		if (board[sourceX][sourceY] == space)
		{
			return "Error! \nSource coordinate\n is empty.";
		} else if (Math.abs(board[sourceX][sourceY]) == pawn)
		{
			if ((aimX != sourceX + 1 && aimX != sourceX + 2) || (aimX == sourceX + 2 && sourceX != 1)
					|| (sourceY != aimY && aimY != sourceY + 1 && aimY != sourceY - 1)
					|| (aimY == sourceY && aimX == sourceX + 1 && board[aimX][aimY] != 0)
					|| (aimY == sourceY + 1 && aimX == sourceX + 1 && board[aimX][aimY] == 0)
					|| (aimY == sourceY - 1 && aimX == sourceX + 1 && board[aimX][aimY] == 0)
					|| (sourceX == 1 && aimX == 3 && sourceY != aimY)
					|| (sourceX == 1 && aimX == 3 && sourceY == aimY && board[aimX][aimY] != 0)
					|| (aimX == sourceX && board[aimX][aimY] != 0))
			{
				return "Error!\nPawn cannot \nmove this \ncoordinate.";
			}
			if (color.contentEquals("Black"))
				board[aimX][aimY] = pawn;
			else
				board[aimX][aimY] = -1 * pawn;
		} else if (Math.abs(board[sourceX][sourceY]) == bishop)
		{
			if (Math.abs(aimX - sourceX) != Math.abs(aimY - sourceY))
			{
				return "Error! \nBishop cannot \nmove this \ncoordinate.";
			}
			String error = moveOfBishop(sourceX, sourceY, aimX, aimY);
			if (error != null)
				return error;
			if (color.contentEquals("Black"))
				board[aimX][aimY] = bishop;
			else
				board[aimX][aimY] = -1 * bishop;
		} else if (Math.abs(board[sourceX][sourceY]) == knight)
		{
			if (!(aimX == sourceX + 2 && (aimY == sourceY - 1 || aimY == sourceY + 1))
					&& !(aimX == sourceX - 2 && (aimY == sourceY - 1 || aimY == sourceY + 1))
					&& !(aimY == sourceY + 2 && (aimX == sourceX - 1 || aimX == sourceX + 1))
					&& !(aimY == sourceY - 2 && (aimX == sourceX - 1 || aimX == sourceX + 1)))
			{
				return "Error! \nKnight cannot \nmove this \ncoordinate.";
			}
			if (color.contentEquals("Black"))
				board[aimX][aimY] = knight;
			else
				board[aimX][aimY] = -1 * knight;
		} else if (Math.abs(board[sourceX][sourceY]) == rook)
		{
			if (!(sourceY == aimY || sourceX == aimX))
			{
				return "Error! \nRook cannot \nmove this \ncoordinate.";
			}
			String error = moveOfRook(sourceX, sourceY, aimX, aimY);
			if (error != null)
				return error;
			if (color.contentEquals("Black"))
				board[aimX][aimY] = rook;
			else
				board[aimX][aimY] = -1 * rook;
		} else if (Math.abs(board[sourceX][sourceY]) == queen)
		{
			if (!(sourceY == aimY || sourceX == aimX) && (Math.abs(aimX - sourceX) != Math.abs(aimY - sourceY)))
			{
				return "Error! \nQueen cannot \nmove this \ncoordinate.";
			}
			if ((Math.abs(aimX - sourceX) == Math.abs(aimY - sourceY)))
			{
				String error = moveOfBishop(sourceX, sourceY, aimX, aimY);
				if (error != null)
					return error;
			} else
			{
				String error = moveOfRook(sourceX, sourceY, aimX, aimY);
				if (error != null)
					return error;
			}
			if (color.contentEquals("Black"))
				board[aimX][aimY] = queen;
			else
				board[aimX][aimY] = -1 * queen;
		} else if (Math.abs(board[sourceX][sourceY]) == king)
		{
			if ((sourceX != aimX - 1 && sourceX != aimX && sourceX != aimX + 1) || sourceY != aimY - 1
					&& sourceY != aimY && sourceY != aimY + 1)
			{
				return "Error! \nKing cannot move\n this coordinate.";
			}
			if (color.contentEquals("Black"))
				board[aimX][aimY] = king;
			else
				board[aimX][aimY] = -1 * king;
		} else
		{
			System.out.println("Error! \nUndefined stone.");
			System.exit(1);
		}
		board[sourceX][sourceY] = 0;
		if (color.contentEquals("White"))
			reverseBoard();
		return null;
	}

	String moveOfBishop(int sourceX, int sourceY, int aimX, int aimY)
	{
		if ((aimX - sourceX) < 0 && (aimY - sourceY) < 0)
		{
			for (int i = sourceX - 1, j = sourceY - 1; i > aimX; --i, --j)
			{
				if (board[i][j] != 0)
				{
					return "Error! \nIn way of \naim there is a \nstone.";
				}
			}
		} else if ((aimX - sourceX) > 0 && (aimY - sourceY) < 0)
		{
			for (int i = sourceX + 1, j = sourceY - 1; i < aimX; ++i, --j)
			{
				if (board[i][j] != 0)
				{
					return "Error! \nIn way of \naim there is a \nstone.";
				}
			}
		} else if ((aimX - sourceX) < 0 && (aimY - sourceY) > 0)
		{
			for (int i = sourceX - 1, j = sourceY + 1; i > aimX; --i, ++j)
			{
				if (board[i][j] != 0)
				{
					return "Error! \nIn way of aim \nthere is a \nstone.";
				}
			}
		} else
		{
			for (int i = sourceX + 1, j = sourceY + 1; i < aimX; ++i, ++j)
			{
				if (board[i][j] != 0)
				{
					return "Error! \nIn way of aim \nthere is a \nstone.";
				}
			}
		}
		return null;
	}

	String moveOfRook(int sourceX, int sourceY, int aimX, int aimY)
	{
		if (aimY == sourceY)
		{
			if (aimX - sourceX > 0)
				for (int i = sourceX + 1; i < aimX; ++i)
				{
					if (board[i][aimY] != 0)
					{
						return "Error! \nIn way of aim \nthere is a \nstone.";
					}
				}
			else
				for (int i = sourceX - 1; i > aimX; --i)
				{
					if (board[i][aimY] != 0)
					{
						return "Error! \nIn way of aim \nthere is a \nstone.";
					}
				}
		} else
		{
			if (aimY - sourceY > 0)
			{
				for (int i = sourceY + 1; i < aimY; ++i)
					if (board[aimX][i] != 0)
					{
						return "Error! Rook cannot move this coordinate.";
					}
			} else
				for (int i = sourceY - 1; i > aimY; --i)
					if (board[aimX][i] != 0)
					{
						return "Error! Rook cannot move this coordinate.";
					}
		}
		return null;
	}

	void displayOnConsole()
	{
		System.out.print("     ");
		for (int i = 0; i < board.length; i++)
		{
			System.out.print(i + 1 + "  ");
		}
		System.out.print("\n  * ");
		for (int i = 0; i < board.length; i++)
		{
			System.out.print(" * ");
		}
		System.out.println();
		for (int i = 0; i < board.length; i++)
		{
			System.out.print(i + 1 + " *");
			for (int j = 0; j < board.length; j++)
			{
				if (board[i][j] == pawn)
					System.out.printf("%3s", "bp");
				else if (board[i][j] == -1 * pawn)
					System.out.printf("%3s", "wp");
				else if (board[i][j] == bishop)
					System.out.printf("%3s", "bb");
				else if (board[i][j] == -1 * bishop)
					System.out.printf("%3s", "wb");
				else if (board[i][j] == knight)
					System.out.printf("%3s", "bh");
				else if (board[i][j] == -1 * knight)
					System.out.printf("%3s", "wh");
				else if (board[i][j] == rook)
					System.out.printf("%3s", "br");
				else if (board[i][j] == -1 * rook)
					System.out.printf("%3s", "wr");
				else if (board[i][j] == queen)
					System.out.printf("%3s", "bq");
				else if (board[i][j] == -1 * queen)
					System.out.printf("%3s", "wq");
				else if (board[i][j] == king)
					System.out.printf("%3s", "bk");
				else if (board[i][j] == -1 * king)
					System.out.printf("%3s", "wk");
				else
					System.out.printf("%3d", board[i][j]);
			}
			System.out.println();
		}
	}

	void reverseBoard()
	{
		int[][] temp = new int[8][8];

		for (int i = 0; i < temp.length; i++)
		{
			temp[i] = new int[8];
		}
		for (int i = 0; i < board.length; i++)
		{
			for (int k = 0; k < board.length; k++)
			{
				temp[i][k] = board[7 - i][k];
			}
		}
		for (int i = 0; i < board.length; i++)
		{
			for (int k = 0; k < board.length; k++)
			{
				board[i][k] = temp[i][k];
			}
		}
	}

	boolean checkKing(String color)
	{
		if (color.contentEquals("White"))
		{
			negativeBoard(color);

			boolean result = checkKingHelper(color);

			negativeBoard(color);

			return result;
		} else
			return checkKingHelper(color);
	}

	boolean checkKingHelper(String color)
	{
		for (int i = 0; i < board.length; i++)
		{
			for (int j = 0; j < board.length; j++)
			{
				if (board[i][j] == pawn)
				{
					if (color.contentEquals("Black"))
					{
						if ((i + 1 < 8 && j - 1 >= 0 && board[i + 1][j - 1] == -king)
								|| (i + 1 < 8 && j + 1 < 8 && board[i + 1][j + 1] == -king))
							return true;
					} else
					{
						if ((i - 1 >= 0 && j - 1 >= 0 && board[i - 1][j - 1] == -king)
								|| (i - 1 >= 0 && j + 1 < 8 && board[i - 1][j + 1] == -king))
							return true;
					}
				} else if (board[i][j] == bishop)
				{
					if (checkKingWithBishop(i, j))
						return true;
				} else if (board[i][j] == knight)
				{
					if ((i + 2 < 8 && j + 1 < 8 && board[i + 2][j + 1] == -king)
							|| (i + 2 < 8 && j - 1 >= 0 && board[i + 2][j - 1] == -king)
							|| (i - 2 >= 0 && j + 1 < 8 && board[i - 2][j + 1] == -king)
							|| (i - 2 >= 0 && j - 1 >= 0 && board[i - 2][j - 1] == -king)
							|| (i - 1 >= 0 && j + 2 < 8 && board[i - 1][j + 2] == -king)
							|| (i + 1 < 8 && j + 2 < 8 && board[i + 1][j + 2] == -king)
							|| (i - 1 >= 0 && j - 2 >= 0 && board[i - 1][j - 2] == -king)
							|| (i + 1 < 8 && j - 2 >= 0 && board[i + 1][j - 2] == -king))
						return true;

				} else if (board[i][j] == rook)
				{
					if (checkKingWithRook(i, j))
						return true;
				} else if (board[i][j] == queen)
				{
					if (checkKingWithBishop(i, j) || checkKingWithRook(i, j))
						return true;
				} else if (board[i][j] == king)
				{
					if ((i + 1 < 8 && j + 1 < 8 && board[i + 1][j + 1] == -king)
							|| (i + 1 < 8 && board[i + 1][j] == -king)
							|| (i + 1 < 8 && j - 1 >= 0 && board[i + 1][j - 1] == -king)
							|| (j + 1 < 8 && board[i][j + 1] == -king) || (j - 1 >= 0 && board[i][j - 1] == -king)
							|| (i - 1 >= 0 && j + 1 < 8 && board[i - 1][j + 1] == -king)
							|| (i - 1 >= 0 && board[i - 1][j] == -king)
							|| (i - 1 >= 0 && j - 1 >= 0 && board[i - 1][j - 1] == -king))
						return true;
				}
			}
		}
		return false;
	}

	boolean checkKingWithBishop(int i, int j)
	{
		for (int i2 = i + 1, j2 = j + 1; i2 < board.length && j2 < board.length; i2++, j2++)
		{
			if (board[i2][j2] != -king && board[i2][j2] != space)
				break;
			if (board[i2][j2] == -king)
				return true;
		}
		for (int i2 = i + 1, j2 = j - 1; i2 < board.length && j2 >= 0; i2++, j2--)
		{
			if (board[i2][j2] != -king && board[i2][j2] != space)
				break;
			if (board[i2][j2] == -king)
				return true;
		}
		for (int i2 = i - 1, j2 = j + 1; i2 >= 0 && j2 < board.length; i2--, j2++)
		{
			if (board[i2][j2] != -king && board[i2][j2] != space)
				break;
			if (board[i2][j2] == -king)
				return true;
		}
		for (int i2 = i - 1, j2 = j - 1; i2 >= 0 && j2 >= 0; i2--, j2--)
		{
			if (board[i2][j2] != -king && board[i2][j2] != space)
				break;
			if (board[i2][j2] == -king)
				return true;
		}
		return false;

	}

	boolean checkKingWithRook(int i, int j)
	{
		for (int i2 = i + 1; i2 < 8; ++i2)
		{
			if (board[i2][j] != -king && board[i2][j] != space)
				break;
			if (board[i2][j] == -king)
				return true;
		}
		for (int i2 = i - 1; i2 >= 0; --i2)
		{
			if (board[i2][j] != -king && board[i2][j] != space)
				break;
			if (board[i2][j] == -king)
				return true;
		}
		for (int j2 = j + 1; j2 < 8; ++j2)
		{
			if (board[i][j2] != -king && board[i][j2] != space)
				break;
			if (board[i][j2] == -king)
				return true;
		}

		for (int j2 = j - 1; j2 >= 0; --j2)
		{
			if (board[i][j2] != -king && board[i][j2] != space)
				break;
			if (board[i][j2] == -king)
				return true;
		}
		return false;
	}

	void backupBoard()
	{
		for (int i = 0; i < board.length; i++)
		{
			for (int j = 0; j < board.length; j++)
			{
				copiedBoard[i][j] = board[i][j];
			}
		}
	}

	void restoreBoard()
	{
		for (int i = 0; i < board.length; i++)
		{
			for (int j = 0; j < board.length; j++)
			{
				board[i][j] = copiedBoard[i][j];
			}
		}
	}

	int[][] findCanMoveField(String color, int x, int y)
	{
		int[][] field = new int[32][2];
		int i = 0;
		negativeBoard(color);
		if (board[x][y] == pawn)
		{
			if (color.contentEquals("Black"))
			{
				if (x + 1 < 8 && board[x + 1][y] == space)
				{
					field[i][0] = x + 1;
					field[i][1] = y;
					++i;
				}
				if (x == 1 && board[x + 2][y] == space)
				{
					field[i][0] = x + 2;
					field[i][1] = y;
					++i;
				}
				if (x + 1 < 8 && y - 1 >= 0 && board[x + 1][y - 1] < 0)
				{
					field[i][0] = x + 1;
					field[i][1] = y - 1;
					++i;
				}
				if (x + 1 < 8 && y + 1 < 8 && board[x + 1][y + 1] < 0)
				{
					field[i][0] = x + 1;
					field[i][1] = y + 1;
					++i;
				}
			} else
			{
				if (x - 1 >= 0 && board[x - 1][y] == space)
				{
					field[i][0] = x - 1;
					field[i][1] = y;
					++i;
				}
				if (x == 6 && board[x - 2][y] == space)
				{
					field[i][0] = x - 2;
					field[i][1] = y;
					++i;
				}
				if (x - 1 >= 0 && y - 1 >= 0 && board[x - 1][y - 1] < 0)
				{
					field[i][0] = x - 1;
					field[i][1] = y - 1;
					++i;
				}
				if (x - 1 >= 0 && y + 1 < 8 && board[x - 1][y + 1] < 0)
				{
					field[i][0] = x - 1;
					field[i][1] = y + 1;
					++i;
				}
			}
		} else if (board[x][y] == bishop)
		{
			int[][] bishopField = new int[32][2];
			bishopField = findCanMoveBishop(x, y);
			for (int j = 0; bishopField[j][0] != -99; j++)
			{
				field[i][0] = bishopField[j][0];
				field[i][1] = bishopField[j][1];
				++i;
			}
		} else if (board[x][y] == knight)
		{
			if (x + 2 < 8 && y - 1 >= 0 && board[x + 2][y - 1] <= 0)
			{
				field[i][0] = x + 2;
				field[i][1] = y - 1;
				++i;
			}
			if (x + 2 < 8 && y + 1 < 8 && board[x + 2][y + 1] <= 0)
			{
				field[i][0] = x + 2;
				field[i][1] = y + 1;
				++i;
			}
			if (x - 2 >= 0 && y + 1 < 8 && board[x - 2][y + 1] <= 0)
			{
				field[i][0] = x - 2;
				field[i][1] = y + 1;
				++i;
			}
			if (x - 2 >= 0 && y - 1 >= 0 && board[x - 2][y - 1] <= 0)
			{
				field[i][0] = x - 2;
				field[i][1] = y - 1;
				++i;
			}
			if (y + 2 < 8 && x - 1 >= 0 && board[x - 1][y + 2] <= 0)
			{
				field[i][0] = x - 1;
				field[i][1] = y + 2;
				++i;
			}
			if (y + 2 < 8 && x + 1 < 8 && board[x + 1][y + 2] <= 0)
			{
				field[i][0] = x + 1;
				field[i][1] = y + 2;
				++i;
			}
			if (y - 2 >= 0 && x + 1 < 8 && board[x + 1][y - 2] <= 0)
			{
				field[i][0] = x + 1;
				field[i][1] = y - 2;
				++i;
			}
			if (y - 2 >= 0 && x - 1 >= 0 && board[x - 1][y - 2] <= 0)
			{
				field[i][0] = x - 1;
				field[i][1] = y - 2;
				++i;
			}
		} else if (board[x][y] == rook)
		{
			int[][] rookField = new int[32][2];
			rookField = findCanMoveRook(x, y);
			for (int j = 0; rookField[j][0] != -99; j++)
			{
				field[i][0] = rookField[j][0];
				field[i][1] = rookField[j][1];
				++i;
			}
		} else if (board[x][y] == queen)
		{
			int[][] bishopField = new int[32][2];
			bishopField = findCanMoveBishop(x, y);
			for (int j = 0; bishopField[j][0] != -99; j++)
			{
				field[i][0] = bishopField[j][0];
				field[i][1] = bishopField[j][1];
				++i;
			}

			int[][] rookField = new int[32][2];
			rookField = findCanMoveRook(x, y);
			for (int j = 0; rookField[j][0] != -99; j++)
			{
				field[i][0] = rookField[j][0];
				field[i][1] = rookField[j][1];
				++i;
			}
		} else if (board[x][y] == king)
		{
			backupBoard();
			String col = "";
			if (color.contentEquals("White"))
				col = "Black";
			else
				col = "White";
			if (x + 1 < 8 && y + 1 < 8 && board[x + 1][y + 1] <= 0)
			{
				negativeBoard(color);
				play(color, x, y, x + 1, y + 1);
				if (!checkKing(col))
				{
					field[i][0] = x + 1;
					field[i][1] = y + 1;
					++i;
				}
				restoreBoard();
			}
			if (x + 1 < 8 && board[x + 1][y] <= 0)
			{
				negativeBoard(color);
				play(color, x, y, x + 1, y);
				if (!checkKing(col))
				{
					field[i][0] = x + 1;
					field[i][1] = y;
					++i;
				}
				restoreBoard();
			}
			if (x + 1 < 8 && y - 1 >= 0 && board[x + 1][y - 1] <= 0)
			{
				negativeBoard(color);
				play(color, x, y, x + 1, y - 1);
				if (!checkKing(col))
				{
					field[i][0] = x + 1;
					field[i][1] = y - 1;
					++i;
				}
				restoreBoard();
			}
			if (y + 1 < 8 && board[x][y + 1] <= 0)
			{
				negativeBoard(color);
				play(color, x, y, x, y + 1);
				if (!checkKing(col))
				{
					field[i][0] = x;
					field[i][1] = y + 1;
					++i;
				}
				restoreBoard();
			}
			if (y - 1 >= 0 && board[x][y - 1] <= 0)
			{
				negativeBoard(color);
				play(color, x, y, x, y - 1);
				if (!checkKing(col))
				{
					field[i][0] = x;
					field[i][1] = y - 1;
					++i;
				}
				restoreBoard();
			}
			if (x - 1 >= 0 && y + 1 < 8 && board[x - 1][y + 1] <= 0)
			{
				negativeBoard(color);
				play(color, x, y, x - 1, y + 1);
				if (!checkKing(col))
				{
					field[i][0] = x - 1;
					field[i][1] = y + 1;
					++i;
				}
				restoreBoard();
			}
			if (x - 1 >= 0 && board[x - 1][y] <= 0)
			{
				negativeBoard(color);
				play(color, x, y, x - 1, y);
				if (!checkKing(col))
				{
					field[i][0] = x - 1;
					field[i][1] = y;
					++i;
				}
				restoreBoard();
			}
			if (x - 1 >= 0 && y - 1 >= 0 && board[x - 1][y - 1] <= 0)
			{
				negativeBoard(color);
				play(color, x, y, x - 1, y - 1);
				if (!checkKing(col))
				{
					field[i][0] = x - 1;
					field[i][1] = y - 1;
					++i;
				}
				restoreBoard();
			}
		} else
			;
		negativeBoard(color);
		field[i][0] = -99;
		return field;
	}

	int[][] findCanMoveBishop(int x, int y)
	{
		int[][] field = new int[32][2];
		int i = 0;

		for (int i2 = x + 1, j2 = y + 1; i2 < board.length && j2 < board.length; i2++, j2++)
		{
			if (board[i2][j2] != space)
			{
				if (board[i2][j2] < 0)
				{
					field[i][0] = i2;
					field[i][1] = j2;
					++i;
				}
				break;
			}
			field[i][0] = i2;
			field[i][1] = j2;
			++i;
		}
		for (int i2 = x + 1, j2 = y - 1; i2 < board.length && j2 >= 0; i2++, j2--)
		{
			if (board[i2][j2] != space)
			{
				if (board[i2][j2] < 0)
				{
					field[i][0] = i2;
					field[i][1] = j2;
					++i;
				}
				break;
			}
			field[i][0] = i2;
			field[i][1] = j2;
			++i;
		}
		for (int i2 = x - 1, j2 = y + 1; i2 >= 0 && j2 < board.length; i2--, j2++)
		{
			if (board[i2][j2] != space)
			{
				if (board[i2][j2] < 0)
				{
					field[i][0] = i2;
					field[i][1] = j2;
					++i;
				}
				break;
			}
			field[i][0] = i2;
			field[i][1] = j2;
			++i;
		}
		for (int i2 = x - 1, j2 = y - 1; i2 >= 0 && j2 >= 0; i2--, j2--)
		{
			if (board[i2][j2] != space)
			{
				if (board[i2][j2] < 0)
				{
					field[i][0] = i2;
					field[i][1] = j2;
					++i;
				}
				break;
			}
			field[i][0] = i2;
			field[i][1] = j2;
			++i;
		}
		field[i][0] = -99;
		return field;
	}

	int[][] findCanMoveRook(int x, int y)
	{
		int[][] field = new int[32][2];
		int i = 0;

		for (int i2 = x + 1; i2 < 8; ++i2)
		{
			if (board[i2][y] != space)
			{
				if (board[i2][y] < 0)
				{
					field[i][0] = i2;
					field[i][1] = y;
					++i;
				}
				break;
			}
			field[i][0] = i2;
			field[i][1] = y;
			++i;
		}
		for (int i2 = x - 1; i2 >= 0; --i2)
		{
			if (board[i2][y] != space)
			{
				if (board[i2][y] < 0)
				{
					field[i][0] = i2;
					field[i][1] = y;
					++i;
				}
				break;
			}
			field[i][0] = i2;
			field[i][1] = y;
			++i;
		}
		for (int j2 = y + 1; j2 < 8; ++j2)
		{
			if (board[x][j2] != space)
			{
				if (board[x][j2] < 0)
				{
					field[i][0] = x;
					field[i][1] = j2;
					++i;
				}
				break;
			}
			field[i][0] = x;
			field[i][1] = j2;
			++i;
		}

		for (int j2 = y - 1; j2 >= 0; --j2)
		{
			if (board[x][j2] != space)
			{
				if (board[x][j2] < 0)
				{
					field[i][0] = x;
					field[i][1] = j2;
					++i;
				}
				break;
			}
			field[i][0] = x;
			field[i][1] = j2;
			++i;
		}

		field[i][0] = -99;
		return field;
	}

	boolean checkMate(String color, int x, int y)
	{
		String col = "";
		if (color.contentEquals("Black"))
			col = "White";
		else
			col = "Black";
		backupBoard();
		play(col, x, y, x + 1, y + 1);
		if (!checkKing(color))
			return false;
		restoreBoard();
		play(col, x, y, x + 1, y);
		if (!checkKing(color))
			return false;
		restoreBoard();
		play(col, x, y, x + 1, y - 1);
		if (!checkKing(color))
			return false;
		restoreBoard();
		play(col, x, y, x, y + 1);
		if (!checkKing(color))
			return false;
		restoreBoard();
		play(col, x, y, x, y - 1);
		if (!checkKing(color))
			return false;
		restoreBoard();
		play(col, x, y, x - 1, y + 1);
		if (!checkKing(color))
			return false;
		restoreBoard();
		play(col, x, y, x - 1, y);
		if (!checkKing(color))
			return false;
		restoreBoard();
		play(col, x, y, x - 1, y - 1);
		if (!checkKing(color))
			return false;
		return true;
	}

	void negativeBoard(String color)
	{
		if (color.contentEquals("White"))
		{
			for (int i2 = 0; i2 < board.length; i2++)
			{
				for (int j = 0; j < board.length; j++)
				{
					board[i2][j] = -board[i2][j];
				}
			}
		}
	}

	void castling(String color, String kind)
	{
		if (kind.contentEquals("Small"))
		{
			if (color.contentEquals("Black"))
			{
				board[0][4] = 0;
				board[0][7] = 0;
				board[0][6] = king;
				board[0][5] = rook;
			} else
			{
				board[7][4] = 0;
				board[7][7] = 0;
				board[7][6] = -king;
				board[7][5] = -rook;
			}
		} else
		{
			if (color.contentEquals("Black"))
			{
				board[0][4] = 0;
				board[0][0] = 0;
				board[0][2] = king;
				board[0][3] = rook;
			} else
			{
				board[7][4] = 0;
				board[7][0] = 0;
				board[7][2] = -king;
				board[7][3] = -rook;
			}
		}
	}

}
