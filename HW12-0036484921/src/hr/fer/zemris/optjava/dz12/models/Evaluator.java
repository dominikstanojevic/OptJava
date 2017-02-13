package hr.fer.zemris.optjava.dz12.models;

/**
 * Created by Dominik on 5.2.2017..
 */
public class Evaluator {
    private Grid template;
    private ThreadLocal<Grid> grids;
    private int numberOfMoves;
    private double plagiarism;

    public Evaluator(Grid template, int numberOfMoves, double plagiarism) {
        this.template = template;
        this.numberOfMoves = numberOfMoves;
        this.plagiarism = plagiarism;

        this.grids = ThreadLocal.withInitial(() -> Grid.copy(template));
    }


    public void evaluate(Ant ant) {
        Grid grid = grids.get();
        Grid.copy(template, grid);

        ant.collected = grid.play(ant, numberOfMoves);
        if(ant.parent != null && ant.collected == ant.parent.collected) {
            ant.fitness = plagiarism * ant.collected;
        } else {
            ant.fitness = Double.valueOf(ant.collected);
        }
    }
}
