import{O as e,R as o,k as a}from"./indexhtml-171d72c6.js";import{checkboxElement as i,checkedCheckboxElement as t}from"./vaadin-checkbox-4e68df64-570c881f.js";const r=[e.textColor,e.fontSize,e.fontWeight,e.fontStyle,o.backgroundColor],c={tagName:"vaadin-grid",displayName:"Grid",elements:[{selector:"vaadin-grid",displayName:"Root element",properties:[o.borderColor,o.borderWidth]},{selector:"vaadin-grid::part(header-cell)",displayName:"Header row cell",properties:[e.textColor,{...e.fontSize,propertyName:"--lumo-font-size-s"},e.fontStyle,o.backgroundColor]},{selector:"vaadin-grid::part(body-cell)",displayName:"Body cell",properties:r},{selector:"vaadin-grid::part(even-row-cell)",displayName:"Cell in even row",properties:r},{selector:"vaadin-grid::part(odd-row-cell)",displayName:"Cell in odd row",properties:r},{selector:"vaadin-grid::part(selected-row-cell)",displayName:"Cell in selected row",properties:r},{selector:"vaadin-grid vaadin-grid-cell-content > vaadin-checkbox::part(checkbox)",displayName:"Row selection checkbox",properties:i.properties},{selector:"vaadin-grid vaadin-grid-cell-content > vaadin-checkbox[checked]::part(checkbox)",displayName:"Row selection checkbox (when checked)",properties:t.properties},{selector:"vaadin-grid vaadin-grid-cell-content > vaadin-checkbox::part(checkbox)::after",displayName:"Row selection checkbox checkmark",properties:[a.iconColor]},{selector:"vaadin-grid vaadin-grid-tree-toggle::part(toggle)",displayName:"Expand icon (for tree grid)",properties:[a.iconColor]}]};export{r as cellProperties,c as default};
