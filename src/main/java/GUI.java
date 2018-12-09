import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

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
    public static Conversion conversion = new Conversion();
    private JPanel panel;
    private JTextField InputFile;
    private JTextField OutputFile;
    private JLabel InputLabel;
    private JLabel OutputLabel;
    private JProgressBar progressBar1;
    private JTextArea Output;
    private JCheckBox OutputEnable;
    private JLabel ScaleRatioLabel;
    private JTextField textField1;
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

    public GUI() {

        /*
         * Conversion mode select
         */
        DenoiseScaleSelect.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                ScaleSelect.setSelected(false);
                DenoiseSelect.setSelected(false);
                conversion.mode = "noise";
            }
        });
        ScaleSelect.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                DenoiseScaleSelect.setSelected(false);
                DenoiseSelect.setSelected(false);
                conversion.mode = "scale";
            }
        });
        DenoiseSelect.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                DenoiseScaleSelect.setSelected(false);
                ScaleSelect.setSelected(false);
                conversion.mode = "noise_scale";
            }
        });

        /*
         * Input/Output File control
         */
        InputFile.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                conversion.input = InputFile.getText();
                OutputFile.setText(conversion.input);
            }
        });

        OutputFile.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                conversion.output = OutputFile.getText() + conversion.getSettings();
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
            }
        });
        Denoise2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                Denoise1.setSelected(false);
                Denoise3.setSelected(false);
                conversion.noise_level = "2";
            }
        });
        Denoise3.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                Denoise1.setSelected(false);
                Denoise2.setSelected(false);
                conversion.noise_level = "3";
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
            }
        });

        DisableGpuButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                conversion.disable_Gpu = DisableGpuButton.isSelected();
            }
        });

        ForceOpenCLButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                conversion.force_Opencl = ForceOpenCLButton.isSelected();
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
            }
        });

        /*
          Launches command
         */
        StartButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                execute(conversion.getSettings());
            }
        });


    }

    public static void main(String[] args) {
        conversion.mode = "noise_scale";
        conversion.block_Size = "256";
        conversion.jobs = "1";
        conversion.scale_Ratio = "1.0";

        JFrame frame = new JFrame("Waifu2x-converter-cpp-gui");
        frame.setContentPane(new GUI().panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    public static void execute(String commandLine) {
        String command = "/usr/bin/x-terminal-emulator -e waifu2x-converter-cpp" + commandLine;

        try {
            Runtime.getRuntime().exec(command);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
