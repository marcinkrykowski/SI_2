package si.csp.utils;

/**
 * @author Jakub Licznerski
 *         Created on 07.04.2017.
 */
public class BaseGraphIterator extends GraphIterator {
    public BaseGraphIterator(int unitCost) {
        super(unitCost);
        this.N = -1; //to avoid initialization errors
        this.cost = 0;
        this.current = new Pointer(-1, 0);
    }

    @Override
    public boolean hasNext() {
        return current.getColIndex() < N - 1 || current.getRowIndex() < N - 1;
    }

    @Override
    public boolean hasPrevious() {
        return current.getColIndex() > 0 || current.getRowIndex() > 0;
    }

    @Override
    public Pointer next() {
        if (current.getColIndex() < N - 1) {
            current.setColIndex(current.getColIndex() + 1);
            increaseCost();
            return current;
        } else if (current.getRowIndex() < N - 1) {
            current.setColIndex(0);
            current.setRowIndex(current.getRowIndex() + 1);
            increaseCost();
            return current;
        }
        throw new IllegalStateException("Called next on the last element");
    }

    @Override
    public Pointer previous() {
        if (current.getColIndex() > 0) {
            current.setColIndex(current.getColIndex() - 1);
            increaseCost();
            return current;
        } else if (current.getRowIndex() > 0) {
            current.setColIndex(N - 1);
            current.setRowIndex(current.getRowIndex() - 1);
            increaseCost();
            return current;
        }
        throw new IllegalStateException("Called previous on the first element");
    }
}
