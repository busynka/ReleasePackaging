import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


/**
 * Created by hvainshtein on 1/6/2016.
 */


public class JFileChooser {

    private JFrame mainFrame;
    private JLabel zipLabel;
    private JLabel confLabel;
    private JLabel dirLabel;
    private JLabel statusLabel;
    //private JPanel controlPanel;
    private JTextField zipFile;
    private JTextField confFile;
    private JTextField dir;
    private JButton selectZip;
    private JButton selectConf;
    private JButton selectDir;


    public JFileChooser(){
        prepareGUI();
    }

    public static void main(String[] args){
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.showFileChooser();
    }

    private void prepareGUI(){
        mainFrame = new JFrame("Release Packaging Tool");
        mainFrame.setSize(600,400);
        //mainFrame.setLayout(new GridLayout(3, 1));
        mainFrame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent windowEvent) {
                System.exit(0);
            }
        });
        zipLabel = new JLabel("", JLabel.LEFT);
        zipLabel.setLocation(5,5);
        zipLabel.setSize(270,10);

        zipFile = new JTextField("", JTextField.LEFT);
        zipFile.setSize(270,30);
        zipFile.setLocation(5, 20);
        zipFile.setVisible(true);

        selectZip = new JButton("Select Zip File");
        selectZip.setSize(200, 30);
        selectZip.setLocation(280, 20);
        selectZip.setVisible(true);

        confLabel = new JLabel("",JLabel.LEFT);
        confLabel.setLocation(5, 70);
        confLabel.setSize(270,10);

        confFile = new JTextField("", JTextField.LEFT);
        confFile.setLocation(5, 90);
        confFile.setSize(270,30);
        confFile.setVisible(true);

        selectConf = new JButton("Select Configuration File");
        selectConf.setLocation(280, 90);
        selectConf.setSize(200, 30);
        selectConf.setVisible(true);

        dirLabel = new JLabel("", JTextField.LEFT);
        dirLabel.setLocation(5,130);
        dirLabel.setSize(270,10);
        dirLabel.setVisible(true);

        dir = new JTextField("", JTextField.LEFT);
        dir.setSize(270,30);
        dir.setLocation(5, 150);
        dir.setVisible(true);

        selectDir = new JButton("Select Destination Directory");
        selectDir.setSize(200, 30);
        selectDir.setLocation(280, 150);
        selectDir.setVisible(true);

        statusLabel = new JLabel("",JLabel.LEFT);
        statusLabel.setSize(270,20);
        statusLabel.setLocation(5,250);

       // controlPanel = new JPanel();
       // controlPanel.setLayout(new FlowLayout());

        mainFrame.add(zipLabel);
       // mainFrame.add(controlPanel);
        mainFrame.add(zipFile);
        mainFrame.add(selectZip);
        mainFrame.add(confLabel);
        mainFrame.add(confFile);
        mainFrame.add(selectConf);
        mainFrame.add(dirLabel);
        mainFrame.add(dir);
        mainFrame.add(selectDir);
        mainFrame.add(statusLabel);
        mainFrame.setVisible(true);
    }

    private void showFileChooser(){
        zipLabel.setText("Please select the source zip file for the release:");

        final javax.swing.JFileChooser fileDialog = new javax.swing.JFileChooser();

        selectZip.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int returnVal = fileDialog.showOpenDialog(mainFrame);
                if (returnVal == javax.swing.JFileChooser.APPROVE_OPTION) {
                    java.io.File file = fileDialog.getSelectedFile();
                    zipFile.setVisible(true);
                    zipFile.setText(file.getAbsolutePath());
                    statusLabel.setText("File Selected :"
                            + file.getName());
                }
                else{
                    statusLabel.setText("Open command cancelled by user." );
                }
            }
        });
        confLabel.setText("Please select configuration file for the release:");
        selectConf.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int returnVal = fileDialog.showOpenDialog(mainFrame);
                if (returnVal == javax.swing.JFileChooser.APPROVE_OPTION) {
                    java.io.File file = fileDialog.getSelectedFile();
                    confFile.setVisible(true);
                    confFile.setText(file.getAbsolutePath());
                    statusLabel.setText("File Selected :"
                            + file.getName());
                }
                else{
                    statusLabel.setText("Open command cancelled by user." );
                }
            }
        });
        dirLabel.setText("Please select the output directory:");
        selectDir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fileDialog.setFileSelectionMode(javax.swing.JFileChooser.DIRECTORIES_ONLY);
                int returnVal = fileDialog.showOpenDialog(mainFrame);
                if (returnVal == javax.swing.JFileChooser.APPROVE_OPTION) {
                    java.io.File file = fileDialog.getCurrentDirectory();
                    dir.setVisible(true);
                    dir.setText(file.getAbsolutePath());
                    statusLabel.setText("File Selected :"
                            + file.getName());
                }
                else{
                    statusLabel.setText("Open command cancelled by user." );
                }
            }
        });
        //controlPanel.add(selectZip);
        //controlPanel.add(selectConf);
        //controlPanel.add(selectDir);
        mainFrame.setVisible(true);
    }
}