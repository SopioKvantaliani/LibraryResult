package com.library.pages;

import com.library.utility.BrowserUtil;
import com.library.utility.Driver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class BookPage extends BasePage {

    @FindBy(xpath = "//table/tbody/tr")
    public List<WebElement> allRows;

    @FindBy(xpath = "//input[@type='search']")
    public WebElement search;

    @FindBy(id = "book_categories")
    public WebElement mainCategoryElement;

    //@FindBy(name = "name")
    @FindBy(xpath = "//tr[@role = 'row']//td[3]")
    public WebElement bookName;

    @FindBy(xpath = "//tr[@role = 'row']//td[5]")
    public WebElement category;


    //@FindBy(xpath = "(//input[@type='text'])[4]")
    @FindBy(xpath = "//tr[@role = 'row']//td[4]")
    public WebElement author;

    @FindBy(xpath = "//div[@class='portlet-title']//a")
    public WebElement addBook;

    @FindBy(xpath = "//button[@type='submit']")
    public WebElement saveChanges;

    @FindBy(xpath = "//div[@class='toast-message']")
    public WebElement toastMessage;

    //@FindBy(name = "year")
    @FindBy(xpath = "//tr[@role = 'row']//td[6]")
    public WebElement year;

    @FindBy(name = "isbn")
    public WebElement isbn;

    @FindBy(xpath = "//tr[@role = 'row']//td[2]")
    public WebElement isbnNumberValue;

    @FindBy(id = "book_group_id")
    public WebElement categoryDropdown;


    @FindBy(id = "description")
    public WebElement description;

    @FindBy(xpath = "//tr[@class = 'odd']//td[5]")
    public WebElement categoryName;


    public WebElement editBook(String book) {
        String xpath = "//td[3][.='" + book + "']/../td/a";
        return Driver.getDriver().findElement(By.xpath(xpath));
    }

    public WebElement borrowBook(String book) {
        String xpath = "//td[3][.='" + book + "']/../td/a";
        return Driver.getDriver().findElement(By.xpath(xpath));
    }


    public  Map<String, String> findSpecificBook(String createdBookName){

        search.sendKeys(createdBookName);

        Map<String, String> bookInfo = new LinkedHashMap<>();

        BrowserUtil.waitFor(2);
        bookInfo.put("isbn", isbnNumberValue.getAccessibleName());
        BrowserUtil.waitFor(2);
        bookInfo.put("name", bookName.getText());
        BrowserUtil.waitFor(2);
        bookInfo.put("author", author.getText());
        BrowserUtil.waitFor(2);
        bookInfo.put("category",categoryName.getText() );
        BrowserUtil.waitFor(2);
        bookInfo.put("year", year.getText());

        return bookInfo;
    }
}
