import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class AddProperty {

    static final String FILE_PATH = "../data/properties.csv";
    static final String IMAGE_FOLDER = "../data/images/";

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> createWindow());
    }


    static void createWindow() {

        JFrame frame = new JFrame("RENTAL FINDER - ADD PROPERTY");

        frame.setSize(700, 820);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(20, 25, 20, 25));


        // ==========================================
        // HEADING
        // ==========================================

        JLabel title = new JLabel("ADD NEW PROPERTY");
        title.setFont(new Font("Arial", Font.BOLD, 24));

        JLabel subtitle = new JLabel(
            "Enter the details of the property you want to list."
        );
        subtitle.setFont(new Font("Arial", Font.PLAIN, 14));

        JPanel heading = new JPanel();
        heading.setLayout(new BoxLayout(heading, BoxLayout.Y_AXIS));
        heading.add(title);
        heading.add(Box.createVerticalStrut(5));
        heading.add(subtitle);

        mainPanel.add(heading, BorderLayout.NORTH);


        // ==========================================
        // FORM
        // ==========================================

        JPanel form = new JPanel(
            new GridLayout(14, 2, 10, 10)
        );

        JTextField locationField = new JTextField();
        JTextField estateField = new JTextField();
        JTextField typeField = new JTextField();
        JTextField rentField = new JTextField();
        JTextField bedroomsField = new JTextField();
        JTextField bathroomsField = new JTextField();
        JTextField distanceField = new JTextField();
        JTextField phoneField = new JTextField();
        JTextField descriptionField = new JTextField();

        JComboBox<String> waterBox =
            new JComboBox<>(new String[]{"Yes", "No"});

        JComboBox<String> parkingBox =
            new JComboBox<>(new String[]{"Yes", "No"});

        JComboBox<String> securityBox =
            new JComboBox<>(new String[]{"Yes", "No"});

        JComboBox<String> furnishedBox =
            new JComboBox<>(new String[]{"Yes", "No"});


        form.add(new JLabel("Location:"));
        form.add(locationField);

        form.add(new JLabel("Estate / Area:"));
        form.add(estateField);

        form.add(new JLabel("Property Type:"));
        form.add(typeField);

        form.add(new JLabel("Monthly Rent (KSh):"));
        form.add(rentField);

        form.add(new JLabel("Bedrooms:"));
        form.add(bedroomsField);

        form.add(new JLabel("Bathrooms:"));
        form.add(bathroomsField);

        form.add(new JLabel("Water:"));
        form.add(waterBox);

        form.add(new JLabel("Parking:"));
        form.add(parkingBox);

        form.add(new JLabel("Security:"));
        form.add(securityBox);

        form.add(new JLabel("Furnished:"));
        form.add(furnishedBox);

        form.add(new JLabel("Distance to Town (km):"));
        form.add(distanceField);

        form.add(new JLabel("Contact Phone:"));
        form.add(phoneField);

        form.add(new JLabel("Description:"));
        form.add(descriptionField);


        // ==========================================
        // PHOTO SELECTION
        // ==========================================

        JLabel imageLabel = new JLabel("No photo selected");

        JButton chooseImageButton =
            new JButton("CHOOSE PHOTO");

        chooseImageButton.setFocusPainted(false);


        JPanel imagePanel = new JPanel(
            new BorderLayout(5, 0)
        );

        imagePanel.add(
            chooseImageButton,
            BorderLayout.WEST
        );

        imagePanel.add(
            imageLabel,
            BorderLayout.CENTER
        );


        form.add(new JLabel("Property Photo:"));
        form.add(imagePanel);


        mainPanel.add(form, BorderLayout.CENTER);


        // ==========================================
        // BUTTONS
        // ==========================================

        JPanel buttonPanel = new JPanel();

        JButton saveButton =
            new JButton("SAVE PROPERTY");

        JButton clearButton =
            new JButton("CLEAR");

        saveButton.setFont(
            new Font("Arial", Font.BOLD, 14)
        );

        clearButton.setFont(
            new Font("Arial", Font.BOLD, 14)
        );

        buttonPanel.add(saveButton);
        buttonPanel.add(clearButton);

        mainPanel.add(
            buttonPanel,
            BorderLayout.SOUTH
        );


        // ==========================================
        // SELECTED IMAGE
        // ==========================================

        final File[] selectedImage = {null};


        chooseImageButton.addActionListener(e -> {

            JFileChooser fileChooser =
                new JFileChooser();

            fileChooser.setDialogTitle(
                "Select Property Photo"
            );

            int result =
                fileChooser.showOpenDialog(frame);

            if (result == JFileChooser.APPROVE_OPTION) {

                selectedImage[0] =
                    fileChooser.getSelectedFile();

                imageLabel.setText(
                    selectedImage[0].getName()
                );
            }
        });


        // ==========================================
        // CLEAR
        // ==========================================

        clearButton.addActionListener(e -> {

            locationField.setText("");
            estateField.setText("");
            typeField.setText("");
            rentField.setText("");
            bedroomsField.setText("");
            bathroomsField.setText("");
            distanceField.setText("");
            phoneField.setText("");
            descriptionField.setText("");

            waterBox.setSelectedIndex(0);
            parkingBox.setSelectedIndex(0);
            securityBox.setSelectedIndex(0);
            furnishedBox.setSelectedIndex(0);

            selectedImage[0] = null;

            imageLabel.setText(
                "No photo selected"
            );
        });


        // ==========================================
        // SAVE PROPERTY
        // ==========================================

        saveButton.addActionListener(e -> {

            try {

                String location =
                    locationField.getText().trim();

                String estate =
                    estateField.getText().trim();

                String type =
                    typeField.getText().trim();


                int rent =
                    Integer.parseInt(
                        rentField.getText().trim()
                    );

                int bedrooms =
                    Integer.parseInt(
                        bedroomsField.getText().trim()
                    );

                int bathrooms =
                    Integer.parseInt(
                        bathroomsField.getText().trim()
                    );

                double distance =
                    Double.parseDouble(
                        distanceField.getText().trim()
                    );


                String water =
                    waterBox.getSelectedItem().toString();

                String parking =
                    parkingBox.getSelectedItem().toString();

                String security =
                    securityBox.getSelectedItem().toString();

                String furnished =
                    furnishedBox.getSelectedItem().toString();


                String phone =
                    phoneField.getText().trim();

                String description =
                    descriptionField.getText().trim();


                // ==========================================
                // VALIDATION
                // ==========================================

                if (
                    location.isEmpty()
                    || estate.isEmpty()
                    || type.isEmpty()
                    || phone.isEmpty()
                    || description.isEmpty()
                ) {

                    JOptionPane.showMessageDialog(
                        frame,
                        "Please fill in all required fields.",
                        "Missing Information",
                        JOptionPane.WARNING_MESSAGE
                    );

                    return;
                }


                // ==========================================
                // GET NEW ID
                // ==========================================

                int newId =
                    getNextId();


                // ==========================================
                // SAVE IMAGE
                // ==========================================

                String imageFileName =
                    "No image";

                if (selectedImage[0] != null) {

                    String originalName =
                        selectedImage[0].getName();

                    String extension = "";

                    int dot =
                        originalName.lastIndexOf(".");

                    if (dot >= 0) {

                        extension =
                            originalName.substring(dot);
                    }

                    imageFileName =
                        "property_"
                        + newId
                        + extension;

                    File destination =
                        new File(
                            IMAGE_FOLDER
                            + imageFileName
                        );

                    File imageDirectory =
                        new File(IMAGE_FOLDER);

                    if (!imageDirectory.exists()) {

                        imageDirectory.mkdirs();
                    }

                    Files.copy(
                        selectedImage[0].toPath(),
                        destination.toPath(),
                        StandardCopyOption.REPLACE_EXISTING
                    );
                }


                // ==========================================
                // CREATE CSV RECORD
                // ==========================================

                String newProperty =
                    newId + ","
                    + csv(location) + ","
                    + csv(estate) + ","
                    + csv(type) + ","
                    + rent + ","
                    + bedrooms + ","
                    + bathrooms + ","
                    + water + ","
                    + parking + ","
                    + security + ","
                    + furnished + ","
                    + distance + " km,"
                    + csv(phone) + ","
                    + csv(description) + ","
                    + csv(imageFileName)
                    + System.lineSeparator();


                // ==========================================
                // WRITE TO CSV
                // ==========================================

                FileWriter writer =
                    new FileWriter(
                        FILE_PATH,
                        true
                    );

                writer.write(newProperty);

                writer.close();


                // ==========================================
                // SUCCESS
                // ==========================================

                JOptionPane.showMessageDialog(
                    frame,
                    "Property #"
                    + newId
                    + " has been added successfully!",
                    "Property Saved",
                    JOptionPane.INFORMATION_MESSAGE
                );


                clearButton.doClick();


            } catch (NumberFormatException ex) {

                JOptionPane.showMessageDialog(
                    frame,
                    "Rent, bedrooms, bathrooms and distance must contain valid numbers.",
                    "Invalid Number",
                    JOptionPane.ERROR_MESSAGE
                );


            } catch (IOException ex) {

                JOptionPane.showMessageDialog(
                    frame,
                    "Could not save the property.\n\n"
                    + ex.getMessage(),
                    "File Error",
                    JOptionPane.ERROR_MESSAGE
                );


            } catch (Exception ex) {

                JOptionPane.showMessageDialog(
                    frame,
                    "Something went wrong.\n\n"
                    + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
                );
            }
        });


        // ==========================================
        // DISPLAY
        // ==========================================

        frame.add(mainPanel);

        frame.setVisible(true);
    }


    // ==========================================
    // GET NEXT PROPERTY ID
    // ==========================================

    static int getNextId() throws IOException {

        File file =
            new File(FILE_PATH);

        int highestId = 0;

        BufferedReader reader =
            new BufferedReader(
                new FileReader(file)
            );

        String line;

        reader.readLine();

        while (
            (line = reader.readLine())
                != null
        ) {

            if (line.trim().isEmpty()) {
                continue;
            }

            String[] parts =
                line.split(",", 2);

            try {

                int id =
                    Integer.parseInt(
                        parts[0].trim()
                    );

                if (id > highestId) {
                    highestId = id;
                }

            } catch (NumberFormatException ignored) {
            }
        }

        reader.close();

        return highestId + 1;
    }


    // ==========================================
    // CSV HELPER
    // ==========================================

    static String csv(String value) {

        if (
            value.contains(",")
            || value.contains("\"")
            || value.contains("\n")
        ) {

            value =
                value.replace(
                    "\"",
                    "\"\""
                );

            return "\"" + value + "\"";
        }

        return value;
    }
}