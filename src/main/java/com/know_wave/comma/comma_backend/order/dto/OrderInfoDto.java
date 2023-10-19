package com.know_wave.comma.comma_backend.order.dto;

import com.know_wave.comma.comma_backend.order.entity.Subject;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


public record OrderInfoDto(Subject subject) {

}
