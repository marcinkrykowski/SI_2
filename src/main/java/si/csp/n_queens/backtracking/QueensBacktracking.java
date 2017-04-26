package si.csp.n_queens.backtracking;

import si.csp.Runner;
import si.csp.n_queens.QueensCSPStrategy;
import si.csp.utils.BoardIterator;
import si.csp.utils.Pointer;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * @author Jakub Licznerski
 *         Created on 25.04.2017.
 */
public class QueensBacktracking extends QueensCSPStrategy {
    private List<boolean[][]> result;

    public QueensBacktracking(int n, BoardIterator iterator) {
        super(n, iterator);
    }

    @Override
    public List<boolean[][]> solve() {
        result = new ArrayList<>();

        Pointer next = iterator.next();
        while (next != null) {
            next = stepForward(next);
            if (next != null)
                next = stepBackward(next);
        }
        return result;
    }

    private Pointer stepForward(Pointer current) {
//        Runner.displayResults(getCurrentSolution());
//        if (currentQueens >= N) {
//            saveSolution(getCurrentSolution());
//            if (checkConstraints(current)) {
//                setField(current, true);
//                if (iterator.hasNext()) { //set and continue
//                    return stepForward(iterator.nextLevel());
//                } else { //we are at the last node, add result
//
//                    setField(current, false);
//                    return iterator.previous();
//                }
//            } else { //constraints not met for this field
////            if (!iterator.hasPrevious())
////                throw new IllegalStateException("Wywołano wstecz w metodzie forward");
//                return stepForward(iterator.next());
//            }
//        }

        if (checkConstraints(current)) { //mozna tu postawic
            setField(current, true);
            currentQueens++;
            if (currentQueens >= N) { //mamy rozwiazanie
                saveSolution(getCurrentSolution());
                clearRow(current.getRowIndex());
                currentQueens--;
                return iterator.previousLevel(); //doesnt need check
            } else { //to jeszcze nie rozwiązanie
                return stepForward(iterator.nextLevel()); //todo czy mozna byc exception ?
            }
        } else { //nie mozna tu postawic
            if (iterator.hasNext())
                return stepForward(iterator.next());
            else if (iterator.hasPreviousLevel())
                return iterator.previousLevel();
            else
                return null; //we are in the first row and last value end of algorithm
        }

    }

    private Pointer stepBackward(Pointer current) {
        //start from the first element in the row
//        Runner.displayResults(getCurrentSolution());
//        if (checkConstraints(current)) {
//            setField(current, true);
//            return iterator.next();
//        } else {
//            setField(current, false);
//            if (iterator.hasPrevious()) {
//                return stepBackward(iterator.previous());
//            } else {
//                return null;
//            }
//        }

        //rewind to the field on which is a queen
        Pointer rewind = new Pointer(current);
        while (iterator.hasNext() && !isQueenAt(rewind)) {
            rewind = iterator.next();
        }

        setField(current, false);
        currentQueens--;
        if (iterator.hasNext()) {
            return iterator.next();
        } else if (iterator.hasPreviousLevel()) {
            return stepBackward(iterator.previousLevel());
        } else
            return null; //todo inspect
    }

    private boolean checkConstraints(Pointer current) {

        //horizontal, vertical and diagonal sweep
        for (int i = 0; i < this.board.length; i++) {
            //it checks also the current pointer (if there is already a queen, constraints not met)
            if (board[i][current.getRowIndex()]) {
                return false;
            }
            if (board[current.getColIndex()][i]) {
                return false;
            }

            if (Stream.of(
                    Pointer.build(current.getColIndex() - i, current.getRowIndex() - i, N),
                    Pointer.build(current.getColIndex() + i, current.getRowIndex() - i, N),
                    Pointer.build(current.getColIndex() - i, current.getRowIndex() + i, N),
                    Pointer.build(current.getColIndex() + i, current.getRowIndex() + i, N)
            )
                    .filter(Objects::nonNull)
                    .anyMatch(this::isQueenAt)) {
                return false;
            }
        }

        return true;
    }

    private boolean[][] getCurrentSolution() {
        boolean[][] result = new boolean[N][N];
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {
                result[i][j] = board[i][j];
            }
        }
        return result;
    }

    private void saveSolution(boolean[][] solution) {
        result.add(solution);
    }
}