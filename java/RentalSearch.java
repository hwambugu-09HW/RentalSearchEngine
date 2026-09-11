import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.*;
import java.util.*;

public class RentalSearch {

    static final Color DARK = new Color(35, 45, 55);
    static final Color LIGHT = new Color(245, 247, 250);
    static final Color WHITE = Color.WHITE;
    static final Color BORDER = new Color(210, 215, 220);
    static final Color GREEN = new Color(40, 150, 90);

    public static void main(String[] args) {
        SwingUtilities.invokeLater(RentalSearch::createMainWindow);
    }

    public static void createMainWindow() {

        JFrame frame = new JFrame("Rental Finder");
        frame.setSize(600, 650);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.getContentPane().setBackground(LIGHT);

        // ==========================================
        // HEADER
        // ==========================================

        JPanel header = new JPanel();
        header.setBackground(DARK);
        header.setBorder(new EmptyBorder(25, 25, 25, 25));
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("RENTAL FINDER");
        title.setForeground(WHITE);
        title.setFont(new Font("Arial", Font.BOLD, 28));

        JLabel subtitle = new JLabel(
                "Find a home that fits your needs"
        );
        subtitle.setForeground(new Color(220, 225, 230));
        subtitle.setFont(new Font("Arial", Font.PLAIN, 14));

        header.add(title);
        header.add(Box.createVerticalStrut(6));
        header.add(subtitle);

        frame.add(header, BorderLayout.NORTH);

        // ==========================================
        // FORM
        // ==========================================

        JPanel form = new JPanel();
        form.setBackground(LIGHT);
        form.setBorder(new EmptyBorder(25, 35, 15, 35));
        form.setLayout(new GridLayout(0, 2, 12, 12));

        JLabel locationLabel = new JLabel("Location");
        JTextField locationField = new JTextField();

        JLabel rentLabel = new JLabel("Maximum Rent");
        JTextField rentField = new JTextField();

        JLabel bedroomLabel = new JLabel("Bedrooms");

        JComboBox<String> bedroomBox =
                new JComboBox<>(
                        new String[]{
                                "Any",
                                "0",
                                "1",
                                "2",
                                "3",
                                "4",
                                "5"
                        }
                );

        JLabel waterLabel = new JLabel("Water");

        JComboBox<String> waterBox =
                new JComboBox<>(
                        new String[]{
                                "Any",
                                "Yes",
                                "No"
                        }
                );

        JLabel parkingLabel = new JLabel("Parking");

        JComboBox<String> parkingBox =
                new JComboBox<>(
                        new String[]{
                                "Any",
                                "Yes",
                                "No"
                        }
                );

        JLabel sortLabel = new JLabel("Sort By");

        JComboBox<String> sortBox =
                new JComboBox<>(
                        new String[]{
                                "Rent: Low to High",
                                "Rent: High to Low",
                                "Distance to Town"
                        }
                );

        form.add(locationLabel);
        form.add(locationField);

        form.add(rentLabel);
        form.add(rentField);

        form.add(bedroomLabel);
        form.add(bedroomBox);

        form.add(waterLabel);
        form.add(waterBox);

        form.add(parkingLabel);
        form.add(parkingBox);

        form.add(sortLabel);
        form.add(sortBox);

        frame.add(form, BorderLayout.CENTER);

        // ==========================================
        // BUTTONS
        // ==========================================

        JPanel buttons = new JPanel();
        buttons.setBackground(LIGHT);
        buttons.setBorder(new EmptyBorder(10, 35, 30, 35));
        buttons.setLayout(new GridLayout(1, 3, 10, 10));

        JButton searchButton =
                new JButton("SEARCH");

        JButton clearButton =
                new JButton("CLEAR");

        JButton addPropertyButton =
                new JButton("ADD PROPERTY");

        searchButton.setFont(
                new Font("Arial", Font.BOLD, 14)
        );

        // ==========================================
        // CLEAR
        // ==========================================

        clearButton.addActionListener(e -> {

            locationField.setText("");
            rentField.setText("");

            bedroomBox.setSelectedIndex(0);
            waterBox.setSelectedIndex(0);
            parkingBox.setSelectedIndex(0);
            sortBox.setSelectedIndex(0);

        });

        // ==========================================
        // ADD PROPERTY
        // ==========================================

        addPropertyButton.addActionListener(e -> {

            try {

                ProcessBuilder processBuilder =
                        new ProcessBuilder(
                                "java",
                                "AddProperty"
                        );

                processBuilder.directory(
                        new File(".")
                );

                processBuilder.start();

            } catch (Exception ex) {

                JOptionPane.showMessageDialog(
                        frame,
                        "Could not open Add Property.\n\n"
                                + ex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        });

        // ==========================================
        // SEARCH
        // ==========================================

        searchButton.addActionListener(e -> {

            String searchLocation =
                    locationField.getText().trim();

            String maxRent =
                    rentField.getText().trim();

            String bedrooms;

            if (bedroomBox.getSelectedIndex() == 0) {

                bedrooms = "Any";

            } else {

                bedrooms =
                        bedroomBox
                                .getSelectedItem()
                                .toString();
            }

            String water =
                    waterBox
                            .getSelectedItem()
                            .toString();

            String parking =
                    parkingBox
                            .getSelectedItem()
                            .toString();

            String sortOption;

            if (sortBox.getSelectedIndex() == 0) {

                sortOption = "1";

            } else if (sortBox.getSelectedIndex() == 1) {

                sortOption = "2";

            } else {

                sortOption = "3";
            }

            // ==========================================
            // 23C: LOCATION IS OPTIONAL
            // ==========================================

            if (maxRent.isEmpty()) {

                JOptionPane.showMessageDialog(
                        frame,
                        "Please enter maximum rent.",
                        "Missing Information",
                        JOptionPane.WARNING_MESSAGE
                );

                return;
            }

            if (searchLocation.isEmpty()) {

                searchLocation = "Any";
            }

            // ==========================================
            // VALIDATE NUMBERS
            // ==========================================

            try {

                Integer.parseInt(maxRent);

                if (!bedrooms.equalsIgnoreCase("Any")) {

                    Integer.parseInt(bedrooms);
                }

                // ==========================================
                // START PYTHON SEARCH
                // ==========================================

                ProcessBuilder processBuilder =
                        new ProcessBuilder(
                                "python",
                                "../python/search_engine.py",
                                searchLocation,
                                maxRent,
                                bedrooms,
                                water,
                                parking,
                                sortOption
                        );

                processBuilder.redirectErrorStream(true);

                Process process =
                        processBuilder.start();

                BufferedReader reader =
                        new BufferedReader(
                                new InputStreamReader(
                                        process.getInputStream()
                                )
                        );

                // ==========================================
                // RESULTS WINDOW
                // ==========================================

                JFrame resultFrame =
                        new JFrame(
                                "Rental Finder - Results"
                        );

                resultFrame.setSize(850, 780);
                resultFrame.setLayout(
                        new BorderLayout()
                );
                resultFrame.setDefaultCloseOperation(
                        JFrame.DISPOSE_ON_CLOSE
                );

                // ==========================================
                // RESULTS HEADER
                // ==========================================

                JPanel resultHeader =
                        new JPanel(
                                new BorderLayout()
                        );

                resultHeader.setBackground(DARK);
                resultHeader.setBorder(
                        new EmptyBorder(
                                18,
                                25,
                                18,
                                25
                        )
                );

                JLabel resultTitle =
                        new JLabel(
                                "AVAILABLE PROPERTIES"
                        );

                resultTitle.setForeground(WHITE);
                resultTitle.setFont(
                        new Font(
                                "Arial",
                                Font.BOLD,
                                22
                        )
                );

                JLabel searchSummary =
                        new JLabel(
                                searchLocation
                                        + "  |  Max Ksh "
                                        + maxRent
                        );

                searchSummary.setForeground(
                        new Color(
                                220,
                                225,
                                230
                        )
                );

                resultHeader.add(
                        resultTitle,
                        BorderLayout.NORTH
                );

                resultHeader.add(
                        searchSummary,
                        BorderLayout.SOUTH
                );

                resultFrame.add(
                        resultHeader,
                        BorderLayout.NORTH
                );

                // ==========================================
                // RESULTS PANEL
                // ==========================================

                JPanel resultsPanel =
                        new JPanel();

                resultsPanel.setLayout(
                        new BoxLayout(
                                resultsPanel,
                                BoxLayout.Y_AXIS
                        )
                );

                resultsPanel.setBackground(LIGHT);
                resultsPanel.setBorder(
                        new EmptyBorder(
                                20,
                                20,
                                20,
                                20
                        )
                );

                JScrollPane scrollPane =
                        new JScrollPane(
                                resultsPanel
                        );

                scrollPane.setBorder(null);

                int resultCount = 0;

                String line;

                while ((line = reader.readLine()) != null) {

                    if (line.trim().isEmpty()) {
                        continue;
                    }

                    String[] property =
                            line.split("\\|", -1);

                    if (property.length < 15) {
                        continue;
                    }

                    resultCount++;

                    // ==========================================
                    // PROPERTY CARD
                    // ==========================================

                    JPanel card =
                            new JPanel(
                                    new BorderLayout(
                                            15,
                                            10
                                    )
                            );

                    card.setBackground(WHITE);

                    card.setBorder(
                            BorderFactory.createCompoundBorder(
                                    BorderFactory.createLineBorder(
                                            BORDER
                                    ),
                                    new EmptyBorder(
                                            15,
                                            15,
                                            15,
                                            15
                                    )
                            )
                    );

                    card.setMaximumSize(
                            new Dimension(
                                    Integer.MAX_VALUE,
                                    180
                            )
                    );

                    // ==========================================
                    // IMAGE
                    // ==========================================

                    JLabel propertyImage;

                    String imageName =
                            property[14];

                    if (
                            imageName != null
                                    && !imageName.isEmpty()
                                    && !imageName.equals(
                                    "No image"
                            )
                    ) {

                        ImageIcon originalImage =
                                new ImageIcon(
                                        "../data/images/"
                                                + imageName
                                );

                        Image scaledImage =
                                originalImage
                                        .getImage()
                                        .getScaledInstance(
                                                180,
                                                130,
                                                Image.SCALE_SMOOTH
                                        );

                        propertyImage =
                                new JLabel(
                                        new ImageIcon(
                                                scaledImage
                                        )
                                );

                    } else {

                        propertyImage =
                                new JLabel("NO PHOTO");

                        propertyImage.setHorizontalAlignment(
                                SwingConstants.CENTER
                        );

                        propertyImage.setPreferredSize(
                                new Dimension(
                                        140,
                                        110
                                )
                        );
                    }

                    propertyImage.setBorder(
                            BorderFactory.createLineBorder(
                                    BORDER
                            )
                    );

                    card.add(
                            propertyImage,
                            BorderLayout.WEST
                    );

                    // ==========================================
                    // INFORMATION
                    // ==========================================

                    JPanel info =
                            new JPanel();

                    info.setLayout(
                            new BoxLayout(
                                    info,
                                    BoxLayout.Y_AXIS
                            )
                    );

                    info.setBackground(WHITE);

                    JLabel typeLabel =
                            new JLabel(
                                    property[3]
                            );

                    typeLabel.setFont(
                            new Font(
                                    "Arial",
                                    Font.BOLD,
                                    19
                            )
                    );

                    typeLabel.setForeground(GREEN);

                    JLabel locationLabelResult =
                            new JLabel(
                                    property[1]
                                            + " | "
                                            + property[2]
                            );

                    JLabel rentLabelResult =
                            new JLabel(
                                    "Ksh "
                                            + property[4]
                            );

                    rentLabelResult.setFont(
                            new Font(
                                    "Arial",
                                    Font.BOLD,
                                    18
                            )
                    );

                    JLabel features =
                            new JLabel(
                                    property[5]
                                            + " bedrooms  |  "
                                            + property[6]
                                            + " bathroom(s)"
                            );

                    JLabel facilities =
                            new JLabel(
                                    "Water: "
                                            + property[7]
                                            + "   Parking: "
                                            + property[8]
                            );

                    JLabel distance =
                            new JLabel(
                                    property[11]
                                            + " to town"
                            );

                    JButton detailsButton =
                            new JButton(
                                    "VIEW DETAILS"
                            );

                    detailsButton.addActionListener(
                            event ->
                                    showPropertyDetails(
                                            property
                                    )
                    );

                    info.add(typeLabel);
                    info.add(
                            Box.createVerticalStrut(5)
                    );
                    info.add(locationLabelResult);
                    info.add(
                            Box.createVerticalStrut(5)
                    );
                    info.add(rentLabelResult);
                    info.add(
                            Box.createVerticalStrut(5)
                    );
                    info.add(features);
                    info.add(facilities);
                    info.add(distance);
                    info.add(
                            Box.createVerticalStrut(8)
                    );
                    info.add(detailsButton);

                    card.add(
                            info,
                            BorderLayout.CENTER
                    );

                    resultsPanel.add(card);

                    resultsPanel.add(
                            Box.createVerticalStrut(12)
                    );
                }

                reader.close();

                process.waitFor();

                // ==========================================
                // NO RESULTS
                // ==========================================

                if (resultCount == 0) {

                    JLabel noResults =
                            new JLabel(
                                    "No properties found."
                            );

                    noResults.setFont(
                            new Font(
                                    "Arial",
                                    Font.BOLD,
                                    18
                            )
                    );

                    noResults.setAlignmentX(
                            Component.CENTER_ALIGNMENT
                    );

                    resultsPanel.add(noResults);
                }

                resultFrame.add(
                        scrollPane,
                        BorderLayout.CENTER
                );

                resultFrame.setLocationRelativeTo(null);
                resultFrame.setVisible(true);

            } catch (NumberFormatException ex) {

                JOptionPane.showMessageDialog(
                        frame,
                        "Maximum rent and bedrooms must be numbers.",
                        "Invalid Input",
                        JOptionPane.ERROR_MESSAGE
                );

            } catch (Exception ex) {

                JOptionPane.showMessageDialog(
                        frame,
                        "Could not perform search.\n\n"
                                + ex.getMessage(),
                        "Search Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        });

        buttons.add(searchButton);
        buttons.add(clearButton);
        buttons.add(addPropertyButton);

        frame.add(
                buttons,
                BorderLayout.SOUTH
        );

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    // ==========================================
    // PROPERTY DETAILS
    // ==========================================

    public static void showPropertyDetails(
            String[] property
    ) {

        JFrame detailsFrame =
                new JFrame(
                        "Property Details"
                );

        detailsFrame.setSize(600, 650);
        detailsFrame.setLayout(
                new BorderLayout()
        );

        // ==========================================
        // HEADER
        // ==========================================

        JPanel header =
                new JPanel();

        header.setBackground(DARK);
        header.setLayout(
                new BoxLayout(
                        header,
                        BoxLayout.Y_AXIS
                )
        );

        header.setBorder(
                new EmptyBorder(
                        20,
                        25,
                        20,
                        25
                )
        );

        JLabel title =
                new JLabel(
                        property[3]
                );

        title.setForeground(WHITE);
        title.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        24
                )
        );

        JLabel location =
                new JLabel(
                        property[1]
                                + " | "
                                + property[2]
                );

        location.setForeground(
                new Color(
                        220,
                        225,
                        230
                )
        );

        header.add(title);
        header.add(
                Box.createVerticalStrut(5)
        );
        header.add(location);

        detailsFrame.add(
                header,
                BorderLayout.NORTH
        );

        // ==========================================
        // DETAILS
        // ==========================================

        JPanel details =
                new JPanel();

        details.setBackground(LIGHT);
        details.setBorder(
                new EmptyBorder(
                        25,
                        30,
                        25,
                        30
                )
        );

        details.setLayout(
                new BoxLayout(
                        details,
                        BoxLayout.Y_AXIS
                )
        );

        addDetail(
                details,
                "Rent",
                "Ksh " + property[4]
        );

        addDetail(
                details,
                "Bedrooms",
                property[5]
        );

        addDetail(
                details,
                "Bathrooms",
                property[6]
        );

        addDetail(
                details,
                "Water",
                property[7]
        );

        addDetail(
                details,
                "Parking",
                property[8]
        );

        addDetail(
                details,
                "Security",
                property[9]
        );

        addDetail(
                details,
                "Furnished",
                property[10]
        );

        addDetail(
                details,
                "Distance to Town",
                property[11]
        );

        addDetail(
                details,
                "Contact",
                property[12]
        );

        addDetail(
                details,
                "Description",
                property[13]
        );

        // ==========================================
        // IMAGE
        // ==========================================

        String imageName =
                property[14];

        if (
                imageName != null
                        && !imageName.isEmpty()
                        && !imageName.equals("No image")
        ) {

            ImageIcon originalImage =
                    new ImageIcon(
                            "../data/images/"
                                    + imageName
                    );

            Image scaledImage =
                    originalImage
                            .getImage()
                            .getScaledInstance(
                                    300,
                                    220,
                                    Image.SCALE_SMOOTH
                            );

            JLabel imageLabel =
                    new JLabel(
                            new ImageIcon(
                                    scaledImage
                            )
                    );

            imageLabel.setAlignmentX(
                    Component.CENTER_ALIGNMENT
            );

            details.add(
                    Box.createVerticalStrut(15)
            );

            details.add(imageLabel);
        }

        JScrollPane scrollPane =
                new JScrollPane(
                        details
                );

        detailsFrame.add(
                scrollPane,
                BorderLayout.CENTER
        );

        // ==========================================
        // CLOSE BUTTON
        // ==========================================

        JPanel bottom =
                new JPanel();

        JButton closeButton =
                new JButton("CLOSE");

        closeButton.addActionListener(
                e -> detailsFrame.dispose()
        );

        bottom.add(closeButton);

        detailsFrame.add(
                bottom,
                BorderLayout.SOUTH
        );

        detailsFrame.setLocationRelativeTo(null);
        detailsFrame.setVisible(true);
    }

    // ==========================================
    // DETAIL ROW
    // ==========================================

    public static void addDetail(
            JPanel panel,
            String label,
            String value
    ) {

        JPanel row =
                new JPanel(
                        new BorderLayout(
                                10,
                                5
                        )
                );

        row.setBackground(LIGHT);
        row.setBorder(
                new EmptyBorder(
                        5,
                        0,
                        5,
                        0
                )
        );

        JLabel labelText =
                new JLabel(
                        label + ":"
                );

        labelText.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        14
                )
        );

        JLabel valueText =
                new JLabel(
                        value
                );

        row.add(
                labelText,
                BorderLayout.WEST
        );

        row.add(
                valueText,
                BorderLayout.CENTER
        );

        panel.add(row);
    }
}