package br.com.alura.screenmatch.model;

public enum Category {
    ACTION("Action", "Ação"),
    ROMANCE("Romance", "Romance"),
    COMEDY("Comedy", "Comedia"),
    DRAMA("Drama", " Drama"),
    CRIME("Crime", "Crime");

    private String omdbCategory;

    private String categoriaPortugues;

    Category(String omdbCategory, String categoriaPortugues ){
        this.omdbCategory = omdbCategory;
        this.categoriaPortugues = categoriaPortugues;
    }

    public static Category fromString(String text) {
        for (Category category : Category.values()) {
            if (category.omdbCategory.equalsIgnoreCase(text)) {
                return category;
            }
        }
        throw new IllegalArgumentException("No category found for the given string: " + text);
    }

    public static Category fromPortugues(String text) {
        for (Category category : Category.values()) {
            if (category.categoriaPortugues.equalsIgnoreCase(text)) {
                return category;
            }
        }
        throw new IllegalArgumentException("No category found for the given string: " + text);
    }



}
