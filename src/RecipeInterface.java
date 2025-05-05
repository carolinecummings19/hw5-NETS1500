import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.io.File;
import java.util.List;

public class RecipeInterface extends JFrame {
    private JComboBox<String> dietComboBox;
    private JTextField maxTimeField;
    private JTextField minServingsField;
    private JTextField ingredientsField;
    private JPanel resultsPanel;

    public RecipeInterface() {
        setTitle("Recipe Generator");
        setSize(700, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Main panel with padding
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(2, 1));
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));  // top, left, bottom, right padding
        mainPanel.setBackground(new Color(240, 248, 255)); // light blue background

        // Top panel
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new GridLayout(2, 1));
        topPanel.setBackground(new Color(240, 248, 255)); // match background
        topPanel.setBorder(new EmptyBorder(0, 40, 0, 40));

        // Input panel
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(4, 2, 10, 10));
        inputPanel.setBackground(new Color(240, 248, 255)); // match background

        inputPanel.add(new JLabel("Dietary Restriction:"));
        String[] diets = {"None", "Vegetarian", "Gluten-Free", "Keto", "Vegan", "Kosher"};
        dietComboBox = new JComboBox<>(diets);
        inputPanel.add(dietComboBox);

        inputPanel.add(new JLabel("Max Total Time (mins):"));
        maxTimeField = new JTextField();
        inputPanel.add(maxTimeField);
        maxTimeField.setBorder(BorderFactory.createLineBorder(Color.GRAY));

        inputPanel.add(new JLabel("Min Servings:"));
        minServingsField = new JTextField();
        inputPanel.add(minServingsField);
        minServingsField.setBorder(BorderFactory.createLineBorder(Color.GRAY));

        inputPanel.add(new JLabel("Ingredients (comma-separated):"));
        ingredientsField = new JTextField();
        inputPanel.add(ingredientsField);
        ingredientsField.setBorder(BorderFactory.createLineBorder(Color.GRAY));

        topPanel.add(inputPanel, BorderLayout.NORTH);

        // Button panel (centered)
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(new Color(240, 248, 255)); // match background
        JButton generateButton = new JButton("Generate Recipes");
        generateButton.setBackground(new Color(100, 149, 237));
        buttonPanel.add(generateButton);
        topPanel.add(buttonPanel, BorderLayout.SOUTH);

        mainPanel.add(topPanel);

        // Results area
        resultsPanel = new JPanel();
        resultsPanel.setLayout(new BoxLayout(resultsPanel, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(resultsPanel);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        add(mainPanel);

        // Button action
        generateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                generateRecipes();
            }
        });
    }
    private void generateRecipes() {
        String selectedDiet = dietComboBox.getSelectedItem().toString().toLowerCase();
        if (selectedDiet.equals("none")) selectedDiet = null;

        int maxTime = -1;
        int minServings = -1;

        try {
            if (!maxTimeField.getText().isEmpty()) {
                maxTime = Integer.parseInt(maxTimeField.getText());
            }
            if (!minServingsField.getText().isEmpty()) {
                minServings = Integer.parseInt(minServingsField.getText());
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter valid numbers for time and servings.");
            return;
        }

        String[] ingredients = ingredientsField.getText().split(",");
        for (int i = 0; i < ingredients.length; i++) {
            ingredients[i] = ingredients[i].trim().toLowerCase();
        }

        List<String> filters = new ArrayList<>();
        if (maxTime > 0) filters.add("Total Time < " + maxTime + " mins");
        if (minServings > 0) filters.add("Servings > " + minServings);

        List<File> recipesWithRequirements = null;
        if (!filters.isEmpty()) {
            recipesWithRequirements = RecipeScraper.filterRecipeDetails(filters, selectedDiet);
        }

        HashMap<RecipeRanking, Double> rankedRecipes = RecipeScraper.rankRecipesByIngredients(
                Arrays.asList(ingredients), selectedDiet, recipesWithRequirements
        );

        List<Map.Entry<RecipeRanking, Double>> sorted = new ArrayList<>(rankedRecipes.entrySet());
        sorted.sort((e1, e2) -> Double.compare(e2.getValue(), e1.getValue()));

        resultsPanel.removeAll();
        resultsPanel.setLayout(new BoxLayout(resultsPanel, BoxLayout.Y_AXIS));

        int count = 0;
        for (Map.Entry<RecipeRanking, Double> entry : sorted) {
            if (count++ >= 10) break;

            String recipeName = entry.getKey().getName().replace(".txt", "");
            recipeName = recipeName.replace(" Recipe", "");
            JPanel recipePanel = new JPanel();
            recipePanel.setLayout(new BorderLayout());
            recipePanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
            recipePanel.setBackground(new Color(255, 255, 255));

            JToggleButton toggleButton = new JToggleButton(recipeName);
            JPanel detailPanel = new JPanel();
            detailPanel.setLayout(new BoxLayout(detailPanel, BoxLayout.Y_AXIS));
            detailPanel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
            detailPanel.setBackground(new Color(245, 245, 245));

            JEditorPane detailPane = new JEditorPane();
            detailPane.setContentType("text/html");
            detailPane.setText(buildHtmlDetails(entry));
            detailPane.setEditable(false);
            detailPane.setCaretPosition(0);  // scroll to top

            JScrollPane detailScroll = new JScrollPane(detailPane);
            detailScroll.setPreferredSize(new Dimension(500, 200));
            detailPanel.add(detailScroll);

            // First recipe opened by default
            if (count == 1) {
                toggleButton.setSelected(true);
                detailPanel.setVisible(true);
            } else {
                detailPanel.setVisible(false);
            }

            toggleButton.addActionListener(e -> detailPanel.setVisible(toggleButton.isSelected()));

            recipePanel.add(toggleButton, BorderLayout.NORTH);
            recipePanel.add(detailPanel, BorderLayout.CENTER);

            resultsPanel.add(recipePanel);
        }

        if (count == 0) {
            JLabel noResults = new JLabel("No matching recipes found.");
            resultsPanel.add(noResults);
        }

        resultsPanel.revalidate();
        resultsPanel.repaint();
    }

    private String buildHtmlDetails(Map.Entry<RecipeRanking, Double> entry) {
        StringBuilder details = new StringBuilder("<html>");

        for (String detail : entry.getKey().getRecipeDetails().keySet()) {
            details.append("<b>").append(detail).append(":</b> ")
                    .append(entry.getKey().getRecipeDetails().get(detail)[0])
                    .append(" ")
                    .append(entry.getKey().getRecipeDetails().get(detail)[1])
                    .append("<br>");
        }
        details.append("<b>Score:</b> ").append(entry.getValue().toString().substring(0, 5)).append("<br>");

        details.append("<br><b>Full Recipe:</b><br>");
        String fullRecipeText = entry.getKey().getRecipe().replace("\n", "<br>");
        details.append(fullRecipeText);

        details.append("<b>Ingredients missing:</b><br>");
        for (String ingredient : entry.getKey().getIngredientsMissing()) {
            details.append(ingredient).append("<br>");
        }
        details.append("</html>");

        return details.toString();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            RecipeInterface gui = new RecipeInterface();
            gui.setVisible(true);
        });
    }
}