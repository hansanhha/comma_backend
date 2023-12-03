package know_wave.comma.arduino_.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import java.util.List;

@Entity
public class Category {


    protected Category() {}

    public Category(String name) {
        this.name = name;
    }

    @Id
    @GeneratedValue
    @Column(name = "category_id")
    private Long id;

    @Column(unique = true)
    private String name;

    public static boolean isUnMatchCategory(List<Category> findCategories, List<Long> requestCategories) {
        return requestCategories.stream().noneMatch(requestCategory ->
                findCategories.stream().anyMatch(findCategory ->
                        findCategory.getId().equals(requestCategory)
                )
        );
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
