import { element, by, promise, ElementFinder } from 'protractor';

export class ArticleComponentsPage {
    createButton = element(by.id('jh-create-entity'));
    title = element.all(by.css('jhi-article div h2#page-heading span')).first();

    clickOnCreateButton(): promise.Promise<void> {
        return this.createButton.click();
    }

    getTitle(): any {
        return this.title.getAttribute('jhiTranslate');
    }
}

export class ArticleUpdatePage {
    pageTitle = element(by.id('jhi-article-heading'));
    saveButton = element(by.id('save-entity'));
    cancelButton = element(by.id('cancel-save'));
    titleInput = element(by.id('field_title'));
    creationDateInput = element(by.id('field_creationDate'));
    modificationDateInput = element(by.id('field_modificationDate'));
    contentInput = element(by.id('field_content'));
    imageURLInput = element(by.id('field_imageURL'));
    viewsInput = element(by.id('field_views'));
    userSelect = element(by.id('field_user'));
    categorySelect = element(by.id('field_category'));
    tagSelect = element(by.id('field_tag'));
    interestSelect = element(by.id('field_interest'));

    getPageTitle() {
        return this.pageTitle.getAttribute('jhiTranslate');
    }

    setTitleInput(title): promise.Promise<void> {
        return this.titleInput.sendKeys(title);
    }

    getTitleInput() {
        return this.titleInput.getAttribute('value');
    }

    setCreationDateInput(creationDate): promise.Promise<void> {
        return this.creationDateInput.sendKeys(creationDate);
    }

    getCreationDateInput() {
        return this.creationDateInput.getAttribute('value');
    }

    setModificationDateInput(modificationDate): promise.Promise<void> {
        return this.modificationDateInput.sendKeys(modificationDate);
    }

    getModificationDateInput() {
        return this.modificationDateInput.getAttribute('value');
    }

    setContentInput(content): promise.Promise<void> {
        return this.contentInput.sendKeys(content);
    }

    getContentInput() {
        return this.contentInput.getAttribute('value');
    }

    setImageURLInput(imageURL): promise.Promise<void> {
        return this.imageURLInput.sendKeys(imageURL);
    }

    getImageURLInput() {
        return this.imageURLInput.getAttribute('value');
    }

    setViewsInput(views): promise.Promise<void> {
        return this.viewsInput.sendKeys(views);
    }

    getViewsInput() {
        return this.viewsInput.getAttribute('value');
    }

    userSelectLastOption(): promise.Promise<void> {
        return this.userSelect
            .all(by.tagName('option'))
            .last()
            .click();
    }

    userSelectOption(option): promise.Promise<void> {
        return this.userSelect.sendKeys(option);
    }

    getUserSelect(): ElementFinder {
        return this.userSelect;
    }

    getUserSelectedOption() {
        return this.userSelect.element(by.css('option:checked')).getText();
    }

    categorySelectLastOption(): promise.Promise<void> {
        return this.categorySelect
            .all(by.tagName('option'))
            .last()
            .click();
    }

    categorySelectOption(option): promise.Promise<void> {
        return this.categorySelect.sendKeys(option);
    }

    getCategorySelect(): ElementFinder {
        return this.categorySelect;
    }

    getCategorySelectedOption() {
        return this.categorySelect.element(by.css('option:checked')).getText();
    }

    tagSelectLastOption(): promise.Promise<void> {
        return this.tagSelect
            .all(by.tagName('option'))
            .last()
            .click();
    }

    tagSelectOption(option): promise.Promise<void> {
        return this.tagSelect.sendKeys(option);
    }

    getTagSelect(): ElementFinder {
        return this.tagSelect;
    }

    getTagSelectedOption() {
        return this.tagSelect.element(by.css('option:checked')).getText();
    }

    interestSelectLastOption(): promise.Promise<void> {
        return this.interestSelect
            .all(by.tagName('option'))
            .last()
            .click();
    }

    interestSelectOption(option): promise.Promise<void> {
        return this.interestSelect.sendKeys(option);
    }

    getInterestSelect(): ElementFinder {
        return this.interestSelect;
    }

    getInterestSelectedOption() {
        return this.interestSelect.element(by.css('option:checked')).getText();
    }

    save(): promise.Promise<void> {
        return this.saveButton.click();
    }

    cancel(): promise.Promise<void> {
        return this.cancelButton.click();
    }

    getSaveButton(): ElementFinder {
        return this.saveButton;
    }
}
