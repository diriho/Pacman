package Pacman;

public final class SupportMap {

    private static final int[][] intMap = new int[][]{{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, {0, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 0, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 0}, {0, 2, 0, 0, 0, 2, 0, 0, 0, 0, 2, 0, 2, 0, 0, 0, 0, 2, 0, 0, 0, 2, 0}, {0, 3, 0, 0, 0, 2, 0, 0, 0, 0, 2, 0, 2, 0, 0, 0, 0, 2, 0, 0, 0, 3, 0}, {0, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 0}, {0, 2, 0, 0, 0, 2, 0, 2, 0, 0, 0, 0, 0, 0, 0, 2, 0, 2, 0, 0, 0, 2, 0}, {0, 2, 2, 2, 2, 2, 0, 2, 2, 2, 2, 0, 2, 2, 2, 2, 0, 2, 2, 2, 2, 2, 0}, {0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 1, 0, 1, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0}, {0, 0, 0, 0, 0, 2, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 2, 0, 0, 0, 0, 0}, {0, 0, 0, 0, 0, 2, 0, 1, 0, 0, 0, 0, 0, 0, 0, 1, 0, 2, 0, 0, 0, 0, 0}, {0, 0, 0, 0, 0, 2, 0, 1, 0, 1, 1, 5, 1, 1, 0, 1, 0, 2, 0, 0, 0, 0, 0}, {1, 1, 1, 1, 1, 2, 1, 1, 0, 1, 1, 1, 1, 1, 0, 1, 1, 2, 1, 1, 1, 1, 1}, {0, 0, 0, 0, 0, 2, 0, 1, 0, 0, 0, 0, 0, 0, 0, 1, 0, 2, 0, 0, 0, 0, 0}, {0, 0, 0, 0, 0, 2, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 2, 0, 0, 0, 0, 0}, {0, 0, 0, 0, 0, 2, 0, 1, 0, 0, 0, 0, 0, 0, 0, 1, 0, 2, 0, 0, 0, 0, 0}, {0, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 0, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 0}, {0, 2, 0, 0, 0, 2, 0, 0, 0, 0, 2, 0, 2, 0, 0, 0, 0, 2, 0, 0, 0, 2, 0}, {0, 3, 2, 2, 0, 2, 2, 2, 2, 2, 2, 4, 2, 2, 2, 2, 2, 2, 0, 2, 2, 3, 0}, {0, 0, 0, 2, 0, 2, 0, 2, 0, 0, 0, 0, 0, 0, 0, 2, 0, 2, 0, 2, 0, 0, 0}, {0, 2, 2, 2, 2, 2, 0, 2, 2, 2, 2, 0, 2, 2, 2, 2, 0, 2, 2, 2, 2, 2, 0}, {0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0}, {0, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 0}, {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}};

    private static SquareType[][] convertToSquareType(int[][] intMap) {
        SquareType[][] enumMap = new SquareType[intMap.length][intMap[0].length];

        for(int i = 0; i < intMap.length; ++i) {
            for(int j = 0; j < intMap[0].length; ++j) {
                SquareType val = null;
                switch (intMap[i][j]) {
                    case 0:
                        val = SquareType.WALL;
                        break;
                    case 1:
                        val = SquareType.FREE;
                        break;
                    case 2:
                        val = SquareType.DOT;
                        break;
                    case 3:
                        val = SquareType.ENERGIZER;
                        break;
                    case 4:
                        val = SquareType.PACMAN_START_LOCATION;
                        break;
                    case 5:
                        val = SquareType.GHOST_START_LOCATION;
                        break;
                    default:
                        String error = "The board representation must have been updated.\nPlease alert the TA staff that their support code has broken.";
                        throw new IllegalArgumentException(error);
                }

                enumMap[i][j] = val;
            }
        }

        return enumMap;
    }

    public static final SquareType[][] getSupportMap() {
        return convertToSquareType(intMap);
    }

    public static final BoardLocation[][] getMap() {
        return convertToBoardLocation(intMap);
    }

    private static BoardLocation[][] convertToBoardLocation(int[][] intMap) {
        BoardLocation[][] enumMap = new BoardLocation[intMap.length][intMap[0].length];

        for(int i = 0; i < intMap.length; ++i) {
            for(int j = 0; j < intMap[0].length; ++j) {
                BoardLocation val = null;
                switch (intMap[i][j]) {
                    case 0:
                        val = BoardLocation.WALL;
                        break;
                    case 1:
                        val = BoardLocation.FREE;
                        break;
                    case 2:
                        val = BoardLocation.DOT;
                        break;
                    case 3:
                        val = BoardLocation.ENERGIZER;
                        break;
                    case 4:
                        val = BoardLocation.PACMAN_START_LOCATION;
                        break;
                    case 5:
                        val = BoardLocation.GHOST_START_LOCATION;
                        break;
                    default:
                        String error = "The board representation must have been updated.\nPlease alert the TA staff that their support code has broken.";
                        throw new IllegalArgumentException(error);
                }

                enumMap[i][j] = val;
            }
        }

        return enumMap;
    }

}
