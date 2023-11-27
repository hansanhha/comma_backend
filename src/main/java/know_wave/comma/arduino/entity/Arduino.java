package know_wave.comma.arduino.entity;

import know_wave.comma.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;

import java.util.List;
import java.util.Set;

@Entity
public class Arduino extends BaseTimeEntity {

    protected Arduino() {
    }

    public Arduino(String name, int originalCount, String description) {
        this.name = name;
        this.count = originalCount;
        this.originalCount = originalCount;
        this.description = description;
    }

    @Id @GeneratedValue
    @Column(name = "arduino_id")
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Min(0)
    private int count;

    @Column(nullable = false)
    @Min(0)
    private int originalCount;

    @Column(columnDefinition = "TEXT")
    private String description;

    @OneToMany(mappedBy="arduino", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ArduinoCategory> categories;

    @OneToMany(mappedBy = "arduino", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Like> likes;

    @OneToMany(mappedBy = "arduino", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments;

    public boolean isNotEnoughCount(int orderCount) {
        return count < orderCount;
    }

    public void update(String name, int count, int originalCount, String description) {
        this.name = name;
        this.count = count;
        this.originalCount = originalCount;
        this.description = description;
    }

//    @OneToMany(mappedBy = "arduinoItem", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<ArduinoOrder> orders;


    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getCount() {
        return count;
    }

    public int getOriginalCount() {
        return originalCount;
    }

    public String getDescription() {
        return description;
    }

    public Set<Like> getLikes() {
        return likes;
    }

    public int getLikeCount() {
        return likes.size();
    }

    public List<Comment> getComments() {
        return comments;
    }

    public List<String> getCategories() {
        return categories.stream()
                .map(category -> category.getCategory().getName())
                .toList();
    }
}

