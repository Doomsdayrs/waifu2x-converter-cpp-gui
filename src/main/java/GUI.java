import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;

/**
 * This file is part of Waifu2xConverterCppGui.
 * Waifu2xConverterCppGui is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * Foobar is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with Waifu2xConverterCppGui.  If not, see <https://www.gnu.org/licenses/>.
 * ====================================================================
 * Waifu2xConverterCppGui
 * 08 / December / 2018
 *
 * @author github.com/doomsdayrs
 */
public class GUI {
    private Conversion conversion = new Conversion();
    private String[] fileTypes = {"bmp","dib","exr","hdr","jp2","jpe","jpeg","jpg","pbm","pgm","pic","png","pnm","ppm","pxm","ras","sr","tif","tiff","webp"};
    private String fileExtension = "png";
    private File inputFile;
    private boolean outputToText = false;

    private JPanel panel;
    private JTextField InputFile;
    private JTextField OutputFile;
    private JLabel InputLabel;
    private JLabel OutputLabel;
    private JProgressBar progressBar1;
    private JCheckBox OutputEnable;
    private JLabel ScaleRatioLabel;
    private JTextField FileTypesTextInputFeild;
    private JButton StartButton;
    private JPanel ScaleRatioPanel;
    private JPanel FileTypePanel;
    private JPanel ProcessorPanel;
    private JCheckBox DisableGpuButton;
    private JCheckBox ForceOpenCLButton;
    private JComboBox ProcessorSelection;
    private JPanel JpegDenoiseSettingsPannel;
    private JPanel OutputExtensionPannel;
    private JPanel BlockSizePannel;
    private JTextField BlockSizeTextFeild;
    private JLabel BlockSizeLabel;
    private JLabel jpegdenoiselevelSliderLabel;
    private JLabel OutputExtensionLabel;
    private JComboBox ExtensionSelection;
    private JLabel QualityLabel;
    private JComboBox QualitySelection;
    private JPanel ConversionModePanel;
    private JPanel ModelPanel;
    private JLabel ModelLabel;
    private JComboBox ModelSelection;
    private JLabel ConversionModeLabel;
    private JRadioButton DenoiseSelect;
    private JRadioButton ScaleSelect;
    private JRadioButton DenoiseScaleSelect;
    private JTextArea StatusText;
    private JRadioButton Denoise1;
    private JRadioButton Denoise2;
    private JRadioButton Denoise3;
    private JPanel AdvancedSettingsPanel;
    private JLabel AdvancedSettingsLabel;
    private JLabel JobsLabel;
    private JTextField JobsTextField;
    private JTextField ScaleRatioInput;
    private JTextArea OutputTextFeild;
    private JList OutputListFeild;


    public GUI() {

        /*
         * Conversion mode select
         */
        DenoiseScaleSelect.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                ScaleSelect.setSelected(false);
                DenoiseSelect.setSelected(false);
                conversion.mode = "noise_scale";
                updateInOut(inputFile);
            }
        });
        ScaleSelect.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                DenoiseScaleSelect.setSelected(false);
                DenoiseSelect.setSelected(false);
                conversion.mode = "scale";
                updateInOut(inputFile);
            }
        });
        DenoiseSelect.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                DenoiseScaleSelect.setSelected(false);
                ScaleSelect.setSelected(false);
                conversion.mode = "noise";
                updateInOut(inputFile);
            }
        });

        /*
         * Input/Output File control
         */
        InputFile.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                String inputString = InputFile.getText().trim();
                int lastIndexOfSeperator = inputString.lastIndexOf("/") + 1;
                String ParentDir = inputString.substring(0, lastIndexOfSeperator);
                String ChildDir = inputString.substring(lastIndexOfSeperator);
                inputFile = new File(ParentDir, ChildDir);
                if (inputFile.getAbsoluteFile().exists()) {
                    if (inputFile.isFile()) {
                        updateInOut(inputFile);
                    } else if (inputFile.isDirectory()) {
                        updateInOut(inputFile);
                        conversion.recursive_directory = true;
                    } else {
                        updateInOut(inputFile);
                        conversion.recursive_directory = false;
                    }
                } else JOptionPane.showMessageDialog(null, "That is not a valid file!");
            }
        });

        OutputFile.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                conversion.output = OutputFile.getText() + conversion.getOutputHeaders();
            }
        });

        /*
         * Denoise level selection
         */
        Denoise1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                Denoise2.setSelected(false);
                Denoise3.setSelected(false);
                conversion.noise_level = "1";
                updateInOut(inputFile);
            }
        });
        Denoise2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                Denoise1.setSelected(false);
                Denoise3.setSelected(false);
                conversion.noise_level = "2";
                updateInOut(inputFile);
            }
        });
        Denoise3.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                Denoise1.setSelected(false);
                Denoise2.setSelected(false);
                conversion.noise_level = "3";
                updateInOut(inputFile);
            }
        });

        BlockSizeTextFeild.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                boolean isInt = false;
                try {
                    int INT = Integer.parseInt(BlockSizeTextFeild.getText());
                    isInt = true;
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(null, "That is not an integer dumb ass");
                }
                if (isInt)
                    conversion.block_Size = BlockSizeLabel.getText();
                updateInOut(inputFile);
            }
        });

        JobsTextField.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                boolean isInt = false;
                try {
                    int INT = Integer.parseInt(JobsTextField.getText());
                    isInt = true;
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(null, "That is not an integer dumb ass");
                }
                if (isInt)
                    conversion.jobs = JobsTextField.getText();
                updateInOut(inputFile);
            }
        });

        DisableGpuButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                conversion.disable_Gpu = DisableGpuButton.isSelected();
                updateInOut(inputFile);
            }
        });

        ForceOpenCLButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                conversion.force_Opencl = ForceOpenCLButton.isSelected();
                updateInOut(inputFile);
            }
        });

        ScaleRatioInput.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                boolean isInt = false;
                try {
                    double INT = Double.parseDouble(ScaleRatioInput.getText());
                    isInt = true;
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(null, "That is not an double dumb ass");
                }
                if (isInt)
                    conversion.scale_Ratio = ScaleRatioInput.getText();
                updateInOut(inputFile);
            }
        });

        //Fills extension process box
        String temp = "";
        for (int x = 0; x < fileTypes.length; x++) {
            if (x != fileTypes.length - 1)
                temp += fileTypes[x] + ";";
            else temp += fileTypes[x];
        }
        FileTypesTextInputFeild.setText(temp);

        FileTypesTextInputFeild.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                fileTypes = FileTypesTextInputFeild.getText().split(";");
                ExtensionSelection.removeAllItems();
                for (int x = 0; x < fileTypes.length; x++) {
                    ExtensionSelection.addItem(fileTypes[x]);
                }
            }
        });

        //Fills Extension Selection
        for (int x = 0; x < fileTypes.length; x++) {
            ExtensionSelection.addItem(fileTypes[x]);
        }

        ExtensionSelection.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                fileExtension = fileTypes[ExtensionSelection.getSelectedIndex()];
            }
        });


        // Fills processor choice
        String[] processor = new String[0];
        try {
            processor = getProcessors();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        for (int x = 0; x < processor.length; x++)
            ProcessorSelection.addItem(processor[x]);
        //Selects processor
        ProcessorSelection.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                conversion.processor = "" + ProcessorSelection.getSelectedIndex();
            }
        });

        /*
          Launches command
         */
        StartButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                try {
                    execute(conversion.getSettings("'"+OutputFile.getText()+"'"));
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });


        OutputEnable.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                outputToText = OutputEnable.isSelected();
            }
        });
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Waifu2x-converter-cpp-gui");
        frame.setContentPane(new GUI().panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    public void execute(String commandLine) throws IOException, InterruptedException {
        System.out.println("waifu2x-converter-cpp" + commandLine);
        ProcessBuilder processBuilder = new ProcessBuilder("/bin/bash","-c",("waifu2x-converter-cpp" + commandLine));
        Process p = processBuilder.start();
        InputStream is = p.getInputStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String line = null;
        if (outputToText)
        {
        while ((line = br.readLine()) != null) {
            OutputTextFeild.append("\n" + line);
        }
        }
        int r = p.waitFor(); // Let the process finish.

        System.out.println("Finsihed");
    }

    public String[] getProcessors() throws IOException, InterruptedException {
        ArrayList<String> strings = new ArrayList<String>();

        ProcessBuilder processBuilder = new ProcessBuilder("/bin/bash", "-c", "waifu2x-converter-cpp --list-processor");
        Process p = processBuilder.start();
        InputStream is = p.getInputStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String line = null;
        while ((line = br.readLine()) != null) {
            strings.add(line);
        }
        int r = p.waitFor(); // Let the process finish.
        return strings.toArray(new String[strings.size()]);
    }

    public void updateInOut(File file) {
        String absolutePATH = file.getAbsolutePath();
        conversion.input = absolutePATH;
        absolutePATH = absolutePATH.substring(0, absolutePATH.lastIndexOf("."));
        OutputFile.setText(absolutePATH + conversion.getOutputHeaders() + "." + fileExtension);
    }

}
