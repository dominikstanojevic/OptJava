package hr.fer.zemris.optjava.dz12.gui;

import hr.fer.zemris.optjava.dz12.models.Grid;
import hr.fer.zemris.optjava.dz12.models.nodes.Action;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Created by Dominik on 14.2.2017..
 */
public class AntTrailGUI extends JFrame {
    private static final long serialVersionUID = 3426994575222105906L;

    private Action[] actions;
    private int index;

    private GridPanel gridPanel;
    private JButton nextButton;
    private JButton autoButton;
    private JButton stopButton;
    private JButton resetButton;

    private JLabel moves;
    private JLabel lastMove;

    private Timer timer = new Timer(500, new NextActionListener());

    public AntTrailGUI(Grid grid, Action[] actions) {
        this.actions = actions;

        initGUI(grid);

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        pack();
        setVisible(true);

        setTitle("Ant Trail GP (Dominik Stanojevic)");

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                if(timer.isRunning()) {
                    timer.stop();
                }
            }
        });
    }

    private void initGUI(Grid grid) {
        Container cp = getContentPane();
        cp.setLayout(new BorderLayout());

        gridPanel = new GridPanel(grid, actions.length);
        cp.add(gridPanel, BorderLayout.CENTER);

        JPanel statsPanel = new JPanel(new GridLayout(2, 1));
        cp.add(statsPanel, BorderLayout.PAGE_END);

        JPanel buttonsPanel = new JPanel();
        statsPanel.add(buttonsPanel);

        nextButton = new JButton("Next");
        nextButton.addActionListener(new NextActionListener());
        buttonsPanel.add(nextButton);

        autoButton = new JButton("Auto");
        autoButton.addActionListener(new AutoActionListener());
        buttonsPanel.add(autoButton);

        stopButton = new JButton("Stop");
        buttonsPanel.add(stopButton);
        stopButton.addActionListener(new StopActionListener());
        stopButton.setEnabled(false);

        resetButton = new JButton("Reset");
        resetButton.addActionListener(new ResetActionListener());
        buttonsPanel.add(resetButton);

        JPanel movesPanel = new JPanel();
        statsPanel.add(movesPanel);

        moves = new JLabel("Moves: 0/" + actions.length);
        movesPanel.add(moves);

        lastMove = new JLabel("Last move: ");
        movesPanel.add(lastMove);
    }

    class StopActionListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            nextButton.setEnabled(true);
            autoButton.setEnabled(true);
            stopButton.setEnabled(false);

            if (timer.isRunning()) {
                timer.stop();
            }
        }
    }

    class AutoActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            nextButton.setEnabled(false);
            autoButton.setEnabled(false);
            stopButton.setEnabled(true);

            if (!timer.isRunning()) {
                timer.start();
            }
        }
    }

    class NextActionListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            gridPanel.next(actions[index++]);
            moves.setText("Moves: " + index + "/" + actions.length);
            lastMove.setText("Last move: " + actions[index - 1]);

            if (index >= actions.length) {
                nextButton.setEnabled(false);
                autoButton.setEnabled(false);
                stopButton.setEnabled(false);

                if (timer.isRunning()) {
                    timer.stop();
                }
            }
        }
    }

    class ResetActionListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if(timer.isRunning()) {
                timer.stop();
            }

            gridPanel.reset(actions.length);

            nextButton.setEnabled(true);
            autoButton.setEnabled(true);
            stopButton.setEnabled(false);

            index = 0;
        }
    }
}
