import 'construct-style-sheets-polyfill';

const createLinkReferences = (css, target) => {
  // Unresolved urls are written as '@import url(text);' to the css
  // [0] is the full match
  // [1] matches the media query
  // [2] matches the url
  const importMatcher = /(?:@media\s(.+?))?(?:\s{)?\@import\surl\(\s*['"]?(.+?)['"]?\s*\);(?:})?/g;
  
  var match;
  var styleCss = css;
  
  // For each external url import add a link reference
  while((match = importMatcher.exec(css)) !== null) {
    styleCss = styleCss.replace(match[0], "");
    const link = document.createElement('link');
    link.rel = 'stylesheet';
    link.href = match[2];
    if (match[1]) {
      link.media = match[1];
    }
    // For target document append to head else append to target
    if (target === document) {
      document.head.appendChild(link);
    } else {
      target.appendChild(link);
    }
  };
  return styleCss;
};

// target: Document | ShadowRoot
export const injectGlobalCss = (css, target, first) => {
  if(target === document) {
    const hash = getHash(css);
    if (window.Vaadin.theme.injectedGlobalCss.indexOf(hash) !== -1) {
      return;
    }
    window.Vaadin.theme.injectedGlobalCss.push(hash);
  }
  const sheet = new CSSStyleSheet();
  sheet.replaceSync(createLinkReferences(css,target));
  if (first) {
    target.adoptedStyleSheets = [sheet, ...target.adoptedStyleSheets];
  } else {
    target.adoptedStyleSheets = [...target.adoptedStyleSheets, sheet];
  }
};
import stylesCss from 'themes/flowcrmtutorial/styles.css?inline';
import { typography } from '@vaadin/vaadin-lumo-styles/typography.js';
import { color } from '@vaadin/vaadin-lumo-styles/color.js';
import { spacing } from '@vaadin/vaadin-lumo-styles/spacing.js';
import { badge } from '@vaadin/vaadin-lumo-styles/badge.js';

window.Vaadin = window.Vaadin || {};
window.Vaadin.theme = window.Vaadin.theme || {};
window.Vaadin.theme.injectedGlobalCss = [];

/**
 * Calculate a 32 bit FNV-1a hash
 * Found here: https://gist.github.com/vaiorabbit/5657561
 * Ref.: http://isthe.com/chongo/tech/comp/fnv/
 *
 * @param {string} str the input value
 * @returns {string} 32 bit (as 8 byte hex string)
 */
function hashFnv32a(str) {
  /*jshint bitwise:false */
  let i, l, hval = 0x811c9dc5;

  for (i = 0, l = str.length; i < l; i++) {
    hval ^= str.charCodeAt(i);
    hval += (hval << 1) + (hval << 4) + (hval << 7) + (hval << 8) + (hval << 24);
  }

  // Convert to 8 digit hex string
  return ("0000000" + (hval >>> 0).toString(16)).substr(-8);
}

/**
 * Calculate a 64 bit hash for the given input.
 * Double hash is used to significantly lower the collision probability.
 *
 * @param {string} input value to get hash for
 * @returns {string} 64 bit (as 16 byte hex string)
 */
function getHash(input) {
  let h1 = hashFnv32a(input); // returns 32 bit (as 8 byte hex string)
  return h1 + hashFnv32a(h1 + input); 
}
export const applyTheme = (target) => {
  
  injectGlobalCss(stylesCss.toString(), target);
    
  
  if (!document['_vaadintheme_flowcrmtutorial_componentCss']) {
    
    document['_vaadintheme_flowcrmtutorial_componentCss'] = true;
  }
  injectGlobalCss(typography.cssText, target, true);
injectGlobalCss(color.cssText, target, true);
injectGlobalCss(spacing.cssText, target, true);
injectGlobalCss(badge.cssText, target, true);

}
