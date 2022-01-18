package ru.netology.web;

import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;


import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.*;

public class CardDeliveryOrderTest {
    public String plusDays(int plusDays) {
        return LocalDate.now().plusDays(plusDays).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
    }

    @BeforeEach
    public void setUp() {
        Configuration.headless = true;
        open("http://localhost:9999");
        $("[data-test-id=date] input").doubleClick().sendKeys(Keys.chord(Keys.CONTROL, "A"), Keys.DELETE);
    }

    @Test
    public void shouldCardDeliveryOrder() {
        String date = plusDays(3);
        $("[data-test-id=city] input").val("Челябинск");
        $("[data-test-id=date] input").val(date);
        $("[data-test-id=name] input").setValue("Павел Иванов");
        $("[data-test-id=phone] input").val("+79009993344");
        $("[data-test-id=agreement]").click();
        $x("//*[text()='Забронировать']").click();
        $("[data-test-id=notification]")
                .shouldHave(text("Успешно! Встреча успешно забронирована на " + date), Duration.ofSeconds(15));
    }

    @Test
    public void shouldCardDeliveryOrderEmptyFieldCity() {
        String date = plusDays(3);
        $("[data-test-id=date] input").val(date);
        $("[data-test-id=name] input").setValue("Павел Иванов");
        $("[data-test-id=phone] input").val("+79009993344");
        $("[data-test-id=agreement]").click();
        $x("//*[text()='Забронировать']").click();
        $("[data-test-id = 'city'].input_invalid").shouldHave(text("Поле обязательно для заполнения"));
    }

    @Test
    public void shouldCardDeliveryOrderEmptyFieldPhone() {
        String date = plusDays(3);
        $("[data-test-id=city] input").val("Челябинск");
        $("[data-test-id=date] input").val(date);
        $("[data-test-id=name] input").setValue("Павел Иванов");
        $("[data-test-id=agreement]").click();
        $x("//*[text()='Забронировать']").click();
        $("[data-test-id = 'phone'].input_invalid").shouldHave(text("Поле обязательно для заполнения"));
    }

    @Test
    public void shouldCardDeliveryOrderEmptyFieldName() {
        String date = plusDays(3);
        $("[data-test-id=city] input").val("Челябинск");
        $("[data-test-id=date] input").val(date);
        $("[data-test-id=phone] input").val("+79009993344");
        $("[data-test-id=agreement]").click();
        $x("//*[text()='Забронировать']").click();
        $("[data-test-id = 'name'].input_invalid").shouldHave(text("Поле обязательно для заполнения"));
    }

    @Test
    public void shouldCardDeliveryOrderEmptyCheckboxClick() {
        String date = plusDays(3);
        $("[data-test-id=city] input").val("Челябинск");
        $("[data-test-id=date] input").val(date);
        $("[data-test-id=name] input").setValue("Павел Иванов");
        $("[data-test-id=phone] input").val("+79009993344");
        $x("//*[text()='Забронировать']").click();
        $("[data-test-id='agreement'].input_invalid .checkbox__text")
                .shouldHave(exactText("Я соглашаюсь с условиями обработки и использования моих персональных данных"));
    }

    @Test
    public void shouldCardDeliveryOrderInvalidName() {
        String date = plusDays(3);
        $("[data-test-id=city] input").val("Челябинск");
        $("[data-test-id=date] input").val(date);
        $("[data-test-id=name] input").setValue("Pavel Petrov");
        $("[data-test-id=phone] input").val("+79009993344");
        $("[data-test-id=agreement]").click();
        $x("//*[text()='Забронировать']").click();
        $("[data-test-id = 'name'].input_invalid .input__sub")
                .shouldHave(text("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы."));
    }

    @Test
    public void shouldCardDeliveryOrderInvalidCity() {
        String date = plusDays(3);
        $("[data-test-id=city] input").val("Магнитогорск");
        $("[data-test-id=date] input").val(date);
        $("[data-test-id=name] input").setValue("Павел Петров");
        $("[data-test-id=phone] input").val("+79009993344");
        $("[data-test-id=agreement]").click();
        $x("//*[text()='Забронировать']").click();
        $("[data-test-id = 'city'].input_invalid .input__sub")
                .shouldHave(text("Доставка в выбранный город недоступна"));
    }

    @Test
    public void shouldCardDeliveryOrderInvalidDate() {
        String date = plusDays(1);
        $("[data-test-id=city] input").val("Челябинск");
        $("[data-test-id=date] input").val(date);
        $("[data-test-id=name] input").setValue("Павел Иванов");
        $("[data-test-id=phone] input").val("+79009993344");
        $("[data-test-id=agreement]").click();
        $x("//*[text()='Забронировать']").click();
        $("[data-test-id=date] .input_invalid .input__sub")
                .shouldHave(text("Заказ на выбранную дату невозможен"));
    }

}