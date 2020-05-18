import {html, PolymerElement} from '@polymer/polymer/polymer-element.js';
import '@vaadin/vaadin-form-layout/src/vaadin-form-layout.js';
import '@vaadin/vaadin-text-field/src/vaadin-text-field.js';
import '@vaadin/vaadin-text-field/src/vaadin-email-field.js';
import '@vaadin/vaadin-combo-box/src/vaadin-combo-box.js';
import '@vaadin/vaadin-ordered-layout/src/vaadin-horizontal-layout.js';
import '@vaadin/vaadin-button/src/vaadin-button.js';

class ContactForm extends PolymerElement {

    static get template() {
        return html`
<style include="shared-styles">
                :host {
                    display: block;
                    height: 100%;
                }
                /* Workaround for https://github.com/vaadin/flow/issues/8256 */
                :host([hidden]) {
                  display: none !important;
                }
            </style>
<vaadin-form-layout style="width: 100%; height: 100%;">
 <vaadin-text-field label="First name" id="firstName"></vaadin-text-field>
 <vaadin-text-field label="Last name" id="lastName"></vaadin-text-field>
 <vaadin-email-field label="Email" id="email"></vaadin-email-field>
 <vaadin-combo-box label="Company" id="company"></vaadin-combo-box>
 <vaadin-combo-box label="Status" id="status"></vaadin-combo-box>
 <vaadin-horizontal-layout theme="spacing">
  <vaadin-button theme="primary" id="save">
    Save 
  </vaadin-button>
  <vaadin-button theme="error" id="delete">
    Delete 
  </vaadin-button>
  <vaadin-button theme="tertiary" id="close">
    Close 
  </vaadin-button>
 </vaadin-horizontal-layout>
</vaadin-form-layout>
`;
    }

    static get is() {
        return 'contact-form';
    }

    static get properties() {
        return {
            // Declare your properties here.
        };
    }
}

customElements.define(ContactForm.is, ContactForm);
