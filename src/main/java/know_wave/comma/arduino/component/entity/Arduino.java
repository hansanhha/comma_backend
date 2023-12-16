package know_wave.comma.arduino.component.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import know_wave.comma.common.entity.BaseTimeEntity;
import know_wave.comma.common.entity.DeleteEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Entity
@Getter
@Builder
@AllArgsConstructor
public class Arduino extends BaseTimeEntity {

    protected Arduino() {}

    @Id
    @GeneratedValue
    @Column(name = "arduino_id")
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    @Min(value = 0, message="{Min.count}")
    private int count;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    private ArduinoStockStatus stockStatus;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "arduino_category",
            joinColumns = @JoinColumn(name = "arduino_id"),
            inverseJoinColumns = @JoinColumn(name = "arduino_category_id"))
    private List<Category> categories;

    @OneToMany(mappedBy = "arduino", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ArduinoPhoto> photos;

    @Embedded
    private DeleteEntity delete;

    public void update(String updatedArduinoName, int updatedCount, String updatedDescription, List<Category> updatedCategories) {
        this.name = updatedArduinoName;
        this.count = updatedCount;
        this.description = updatedDescription;
        this.categories = updatedCategories;
    }

    public static void delete(Arduino arduino) {
        arduino.delete = DeleteEntity.delete();
    }

    public static void deleteList(List<Arduino> arduinos) {
        arduinos.forEach(Arduino::delete);
    }

    public void addCount(int count) {
        this.count += count;
        updateStockStatus(null);
    }

    public void updateStockStatus(ArduinoStockStatus stockStatus) {
        if (stockStatus != null) {
            this.stockStatus = stockStatus;
        } else if (this.count == 0 && this.stockStatus != ArduinoStockStatus.NONE) {
            this.stockStatus = ArduinoStockStatus.NONE;
        } else if (this.count > 0 && this.stockStatus == ArduinoStockStatus.NONE || this.stockStatus == ArduinoStockStatus.UP_COMMING) {
            if (this.count >= 10) {
                this.stockStatus = ArduinoStockStatus.MORE_THAN_10;
            } else {
                this.stockStatus = ArduinoStockStatus.LESS_THAN_10;
            }
        }
    }

    public void decreaseStock(int orderArduinoCount) {
        this.count -= orderArduinoCount;
        updateStockStatus(null);
    }

    public void increaseStock(int orderArduinoCount) {
        this.count += orderArduinoCount;
        updateStockStatus(null);
    }
}
