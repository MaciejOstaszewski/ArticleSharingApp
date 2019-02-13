import { element, by, promise, ElementFinder } from 'protractor';

export class MessageComponentsPage {
    createButton = element(by.id('jh-create-entity'));
    title = element.all(by.css('jhi-message div h2#page-heading span')).first();

    clickOnCreateButton(): promise.Promise<void> {
        return this.createButton.click();
    }

    getTitle(): any {
        return this.title.getAttribute('jhiTranslate');
    }
}

export class MessageUpdatePage {
    pageTitle = element(by.id('jhi-message-heading'));
    saveButton = element(by.id('save-entity'));
    cancelButton = element(by.id('cancel-save'));
    contentInput = element(by.id('field_content'));
    creationDateInput = element(by.id('field_creationDate'));
    senderSelect = element(by.id('field_sender'));
    receiverSelect = element(by.id('field_receiver'));

    getPageTitle() {
        return this.pageTitle.getAttribute('jhiTranslate');
    }

    setContentInput(content): promise.Promise<void> {
        return this.contentInput.sendKeys(content);
    }

    getContentInput() {
        return this.contentInput.getAttribute('value');
    }

    setCreationDateInput(creationDate): promise.Promise<void> {
        return this.creationDateInput.sendKeys(creationDate);
    }

    getCreationDateInput() {
        return this.creationDateInput.getAttribute('value');
    }

    senderSelectLastOption(): promise.Promise<void> {
        return this.senderSelect
            .all(by.tagName('option'))
            .last()
            .click();
    }

    senderSelectOption(option): promise.Promise<void> {
        return this.senderSelect.sendKeys(option);
    }

    getSenderSelect(): ElementFinder {
        return this.senderSelect;
    }

    getSenderSelectedOption() {
        return this.senderSelect.element(by.css('option:checked')).getText();
    }

    receiverSelectLastOption(): promise.Promise<void> {
        return this.receiverSelect
            .all(by.tagName('option'))
            .last()
            .click();
    }

    receiverSelectOption(option): promise.Promise<void> {
        return this.receiverSelect.sendKeys(option);
    }

    getReceiverSelect(): ElementFinder {
        return this.receiverSelect;
    }

    getReceiverSelectedOption() {
        return this.receiverSelect.element(by.css('option:checked')).getText();
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
