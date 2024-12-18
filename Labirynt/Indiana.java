import java.util.*;

class Indiana implements Explorer {
    private PlayerController controller;
    private int underwaterMovesAllowed;
    private Set<Position> visited = new LinkedHashSet<>();
    private Stack<Direction> pathStack = new Stack<>();
    private Position currentPosition;
    private int movesUnderwater = 0;
    private boolean justMovedUnderwater = false;
    private Set<Position> hitWalls = new LinkedHashSet<>();
    private Set<Position> fireRooms = new LinkedHashSet<>();
    private Set<Position> waterRooms = new LinkedHashSet<>();
    private Position newPosition;
    private Map<Position, Set<Direction>> wallsHit = new HashMap<>();
    private Direction kierunek;

    public Indiana() {
        this.currentPosition = new Position(0, 0);
    }

    @Override
    public void underwaterMovesAllowed(int moves) {
        this.underwaterMovesAllowed = moves;
    }

    @Override
    public void setPlayerController(PlayerController controller) {
        this.controller = controller;
    }

    @Override
    public void findExit() {
        try {
            explore();
        } catch (Exit exit) {
            System.out.println("Exit found!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void explore() throws Exception {
        visited.add(currentPosition);
        for (Direction direction : Direction.values()) {
            newPosition = direction.step(currentPosition);
            if (hitWalls.contains(newPosition) || fireRooms.contains(newPosition)) {
                continue;
            }
            if (!visited.contains(newPosition) && !waterRooms.contains(newPosition)) {
                try {
                    justMovedUnderwater = false;
                    move(direction);
                    currentPosition = newPosition;
                    explore();
                } catch (Flooded flooded) {
                    if (!justMovedUnderwater) {
                        kierunek = direction;
                    }
                    handleFlooded(kierunek);
                } catch (OnFire onFire) {
                    secondHandleOnFire(direction);
                } catch (Wall wall) {
                    handleWall(direction);
                }
            }
        }
        if (!pathStack.isEmpty())  {
            backtrack();
        }
        explore();
    }

    private void move(Direction direction) throws OnFire, Flooded, Wall, Exit {
        try {
            if (!hitWalls.contains(newPosition) && !fireRooms.contains(newPosition)) {
                movesUnderwater = 0;
                controller.move(direction);
                pathStack.push(direction);
            }
        } catch (Wall wall) {
            throw wall;
        }
    }

    private void secondHandleOnFire(Direction direction) throws Flooded, Wall, OnFire, Exit {
        visited.add(newPosition);
        fireRooms.add(newPosition);
        newPosition = getPreviousPosition(newPosition, getOppositeDirection(direction));
        currentPosition = newPosition;
        controller.move(getOppositeDirection(direction));
    }

    private void handleWall(Direction direction) {
        wallsHit.computeIfAbsent(currentPosition, k -> EnumSet.noneOf(Direction.class)).add(direction);
        hitWalls.add(newPosition);
    }

    private void handleFloodedBack(Direction direction) throws Exception {
        try {
            // Jeśli jesteśmy w wodzie, zwiększamy licznik ruchów pod wodą
            if (justMovedUnderwater) {
                movesUnderwater++;
            }
            // Sprawdzamy, czy nie przekroczyliśmy dozwolonej liczby ruchów pod wodą
            if (movesUnderwater >= underwaterMovesAllowed) {
                // Musimy się wynurzyć
                movesUnderwater = 0;
                backtrack();
            } else {
                // Kontynuujemy ruch w wodzie
                if (!hitWalls.contains(currentPosition) && !fireRooms.contains(currentPosition) && !waterRooms.contains(currentPosition)) {
                    currentPosition = newPosition;
                    newPosition = direction.step(currentPosition);
                    justMovedUnderwater = true;
                    pathStack.push(direction);
                    visited.add(currentPosition);
                    waterRooms.add(currentPosition);
                    controller.move(direction);
                    currentPosition = newPosition;
                    pathStack.push(direction);
                    explore();
                } else {
                    // Napotkaliśmy przeszkodę, musimy się cofnąć
                    backtrack();
                }
            }
        } catch (Wall wall) {
            handleWall(direction);
            backtrack();
        }
    }


    private void handleFlooded(Direction direction) throws Exception {
            // Jeśli jesteśmy w wodzie, zwiększamy licznik ruchów pod wodą
            movesUnderwater++;

            // Sprawdzamy, czy nie przekroczyliśmy dozwolonej liczby ruchów pod wodą
            if (movesUnderwater < underwaterMovesAllowed) {
                try {

                if (!hitWalls.contains(newPosition) && !fireRooms.contains(newPosition) && !waterRooms.contains(newPosition)) {
                    // Kontynuujemy ruch w wodzie
                    currentPosition = newPosition;
                    newPosition = direction.step(currentPosition);
                    justMovedUnderwater = true;
                    pathStack.push(direction);
                    visited.add(currentPosition);
                    waterRooms.add(currentPosition);
                    controller.move(direction);
                    currentPosition = newPosition;
                    pathStack.push(direction);
                    explore();
                } else {
                    // Napotkaliśmy przeszkodę, musimy się cofnąć
                    backtrack();
                }
                }catch (Wall wall) {
                    handleWall(direction);
                    backtrack();
                }
            } else {
                backtrack();
            }
    }

    private void backtrack() throws Exception {
        if (!pathStack.isEmpty()) {
            Direction lastDirection = pathStack.pop();
            Direction oppositeDirection = getOppositeDirection(lastDirection);
            Position previousPosition = getPreviousPosition(currentPosition, oppositeDirection);
            currentPosition = previousPosition;
            try {
                if (!hitWalls.contains(currentPosition) && !fireRooms.contains(currentPosition) ) {
                    controller.move(oppositeDirection);
                }
            } catch (OnFire onFire) {
                secondHandleOnFire(oppositeDirection);
            } catch (Wall wall) {
                handleWall(oppositeDirection);
            } catch (Flooded flooded) {
                handleFloodedBack(oppositeDirection);
            }
        }
    }


    private Position getPreviousPosition(Position currentPosition, Direction lastDirection) {
        switch (lastDirection) {
            case NORTH: return new Position(currentPosition.col(), currentPosition.row() + 1);
            case SOUTH: return new Position(currentPosition.col(), currentPosition.row() - 1);
            case EAST: return new Position(currentPosition.col() + 1, currentPosition.row());
            case WEST: return new Position(currentPosition.col() - 1, currentPosition.row());
            default: throw new IllegalStateException("Nieznany kierunek: " + lastDirection);
        }
    }

    private Direction getOppositeDirection(Direction direction) {
        switch (direction) {
            case NORTH: return Direction.SOUTH;
            case SOUTH: return Direction.NORTH;
            case EAST:  return Direction.WEST;
            case WEST:  return Direction.EAST;
            default: throw new IllegalStateException("Nieznany kierunek: " + direction);
        }
    }

}