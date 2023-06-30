try{self["workbox:core:7.0.0"]&&_()}catch{}const M=(a,...e)=>{let t=a;return e.length>0&&(t+=` :: ${JSON.stringify(e)}`),t},S=M;class l extends Error{constructor(e,t){const s=S(e,t);super(s),this.name=e,this.details=t}}const V=new Set,f={googleAnalytics:"googleAnalytics",precache:"precache-v2",prefix:"workbox",runtime:"runtime",suffix:typeof registration<"u"?registration.scope:""},v=a=>[f.prefix,a,f.suffix].filter(e=>e&&e.length>0).join("-"),W=a=>{for(const e of Object.keys(f))a(e)},U={updateDetails:a=>{W(e=>{typeof a[e]=="string"&&(f[e]=a[e])})},getGoogleAnalyticsName:a=>a||v(f.googleAnalytics),getPrecacheName:a=>a||v(f.precache),getPrefix:()=>f.prefix,getRuntimeName:a=>a||v(f.runtime),getSuffix:()=>f.suffix};function P(a,e){const t=new URL(a);for(const s of e)t.searchParams.delete(s);return t.href}async function F(a,e,t,s){const n=P(e.url,t);if(e.url===n)return a.match(e,s);const i=Object.assign(Object.assign({},s),{ignoreSearch:!0}),r=await a.keys(e,i);for(const c of r){const o=P(c.url,t);if(n===o)return a.match(c,s)}}let b;function q(){if(b===void 0){const a=new Response("");if("body"in a)try{new Response(a.body),b=!0}catch{b=!1}b=!1}return b}class H{constructor(){this.promise=new Promise((e,t)=>{this.resolve=e,this.reject=t})}}async function B(){for(const a of V)await a()}const $=a=>new URL(String(a),location.href).href.replace(new RegExp(`^${location.origin}`),"");function j(a){return new Promise(e=>setTimeout(e,a))}function I(a,e){const t=e();return a.waitUntil(t),t}async function G(a,e){let t=null;if(a.url&&(t=new URL(a.url).origin),t!==self.location.origin)throw new l("cross-origin-copy-response",{origin:t});const s=a.clone(),n={headers:new Headers(s.headers),status:s.status,statusText:s.statusText},i=e?e(n):n,r=q()?s.body:await s.blob();return new Response(r,i)}function z(){self.addEventListener("activate",()=>self.clients.claim())}try{self["workbox:precaching:7.0.0"]&&_()}catch{}const Q="__WB_REVISION__";function J(a){if(!a)throw new l("add-to-cache-list-unexpected-type",{entry:a});if(typeof a=="string"){const i=new URL(a,location.href);return{cacheKey:i.href,url:i.href}}const{revision:e,url:t}=a;if(!t)throw new l("add-to-cache-list-unexpected-type",{entry:a});if(!e){const i=new URL(t,location.href);return{cacheKey:i.href,url:i.href}}const s=new URL(t,location.href),n=new URL(t,location.href);return s.searchParams.set(Q,e),{cacheKey:s.href,url:n.href}}class X{constructor(){this.updatedURLs=[],this.notUpdatedURLs=[],this.handlerWillStart=async({request:e,state:t})=>{t&&(t.originalRequest=e)},this.cachedResponseWillBeUsed=async({event:e,state:t,cachedResponse:s})=>{if(e.type==="install"&&t&&t.originalRequest&&t.originalRequest instanceof Request){const n=t.originalRequest.url;s?this.notUpdatedURLs.push(n):this.updatedURLs.push(n)}return s}}}class Y{constructor({precacheController:e}){this.cacheKeyWillBeUsed=async({request:t,params:s})=>{const n=(s==null?void 0:s.cacheKey)||this._precacheController.getCacheKeyForURL(t.url);return n?new Request(n,{headers:t.headers}):t},this._precacheController=e}}try{self["workbox:strategies:7.0.0"]&&_()}catch{}function y(a){return typeof a=="string"?new Request(a):a}class Z{constructor(e,t){this._cacheKeys={},Object.assign(this,t),this.event=t.event,this._strategy=e,this._handlerDeferred=new H,this._extendLifetimePromises=[],this._plugins=[...e.plugins],this._pluginStateMap=new Map;for(const s of this._plugins)this._pluginStateMap.set(s,{});this.event.waitUntil(this._handlerDeferred.promise)}async fetch(e){const{event:t}=this;let s=y(e);if(s.mode==="navigate"&&t instanceof FetchEvent&&t.preloadResponse){const r=await t.preloadResponse;if(r)return r}const n=this.hasCallback("fetchDidFail")?s.clone():null;try{for(const r of this.iterateCallbacks("requestWillFetch"))s=await r({request:s.clone(),event:t})}catch(r){if(r instanceof Error)throw new l("plugin-error-request-will-fetch",{thrownErrorMessage:r.message})}const i=s.clone();try{let r;r=await fetch(s,s.mode==="navigate"?void 0:this._strategy.fetchOptions);for(const c of this.iterateCallbacks("fetchDidSucceed"))r=await c({event:t,request:i,response:r});return r}catch(r){throw n&&await this.runCallbacks("fetchDidFail",{error:r,event:t,originalRequest:n.clone(),request:i.clone()}),r}}async fetchAndCachePut(e){const t=await this.fetch(e),s=t.clone();return this.waitUntil(this.cachePut(e,s)),t}async cacheMatch(e){const t=y(e);let s;const{cacheName:n,matchOptions:i}=this._strategy,r=await this.getCacheKey(t,"read"),c=Object.assign(Object.assign({},i),{cacheName:n});s=await caches.match(r,c);for(const o of this.iterateCallbacks("cachedResponseWillBeUsed"))s=await o({cacheName:n,matchOptions:i,cachedResponse:s,request:r,event:this.event})||void 0;return s}async cachePut(e,t){const s=y(e);await j(0);const n=await this.getCacheKey(s,"write");if(!t)throw new l("cache-put-with-no-response",{url:$(n.url)});const i=await this._ensureResponseSafeToCache(t);if(!i)return!1;const{cacheName:r,matchOptions:c}=this._strategy,o=await self.caches.open(r),h=this.hasCallback("cacheDidUpdate"),g=h?await F(o,n.clone(),["__WB_REVISION__"],c):null;try{await o.put(n,h?i.clone():i)}catch(u){if(u instanceof Error)throw u.name==="QuotaExceededError"&&await B(),u}for(const u of this.iterateCallbacks("cacheDidUpdate"))await u({cacheName:r,oldResponse:g,newResponse:i.clone(),request:n,event:this.event});return!0}async getCacheKey(e,t){const s=`${e.url} | ${t}`;if(!this._cacheKeys[s]){let n=e;for(const i of this.iterateCallbacks("cacheKeyWillBeUsed"))n=y(await i({mode:t,request:n,event:this.event,params:this.params}));this._cacheKeys[s]=n}return this._cacheKeys[s]}hasCallback(e){for(const t of this._strategy.plugins)if(e in t)return!0;return!1}async runCallbacks(e,t){for(const s of this.iterateCallbacks(e))await s(t)}*iterateCallbacks(e){for(const t of this._strategy.plugins)if(typeof t[e]=="function"){const s=this._pluginStateMap.get(t);yield i=>{const r=Object.assign(Object.assign({},i),{state:s});return t[e](r)}}}waitUntil(e){return this._extendLifetimePromises.push(e),e}async doneWaiting(){let e;for(;e=this._extendLifetimePromises.shift();)await e}destroy(){this._handlerDeferred.resolve(null)}async _ensureResponseSafeToCache(e){let t=e,s=!1;for(const n of this.iterateCallbacks("cacheWillUpdate"))if(t=await n({request:this.request,response:t,event:this.event})||void 0,s=!0,!t)break;return s||t&&t.status!==200&&(t=void 0),t}}class N{constructor(e={}){this.cacheName=U.getRuntimeName(e.cacheName),this.plugins=e.plugins||[],this.fetchOptions=e.fetchOptions,this.matchOptions=e.matchOptions}handle(e){const[t]=this.handleAll(e);return t}handleAll(e){e instanceof FetchEvent&&(e={event:e,request:e.request});const t=e.event,s=typeof e.request=="string"?new Request(e.request):e.request,n="params"in e?e.params:void 0,i=new Z(this,{event:t,request:s,params:n}),r=this._getResponse(i,s,t),c=this._awaitComplete(r,i,s,t);return[r,c]}async _getResponse(e,t,s){await e.runCallbacks("handlerWillStart",{event:s,request:t});let n;try{if(n=await this._handle(t,e),!n||n.type==="error")throw new l("no-response",{url:t.url})}catch(i){if(i instanceof Error){for(const r of e.iterateCallbacks("handlerDidError"))if(n=await r({error:i,event:s,request:t}),n)break}if(!n)throw i}for(const i of e.iterateCallbacks("handlerWillRespond"))n=await i({event:s,request:t,response:n});return n}async _awaitComplete(e,t,s,n){let i,r;try{i=await e}catch{}try{await t.runCallbacks("handlerDidRespond",{event:n,request:s,response:i}),await t.doneWaiting()}catch(c){c instanceof Error&&(r=c)}if(await t.runCallbacks("handlerDidComplete",{event:n,request:s,response:i,error:r}),t.destroy(),r)throw r}}class d extends N{constructor(e={}){e.cacheName=U.getPrecacheName(e.cacheName),super(e),this._fallbackToNetwork=e.fallbackToNetwork!==!1,this.plugins.push(d.copyRedirectedCacheableResponsesPlugin)}async _handle(e,t){const s=await t.cacheMatch(e);return s||(t.event&&t.event.type==="install"?await this._handleInstall(e,t):await this._handleFetch(e,t))}async _handleFetch(e,t){let s;const n=t.params||{};if(this._fallbackToNetwork){const i=n.integrity,r=e.integrity,c=!r||r===i;s=await t.fetch(new Request(e,{integrity:e.mode!=="no-cors"?r||i:void 0})),i&&c&&e.mode!=="no-cors"&&(this._useDefaultCacheabilityPluginIfNeeded(),await t.cachePut(e,s.clone()))}else throw new l("missing-precache-entry",{cacheName:this.cacheName,url:e.url});return s}async _handleInstall(e,t){this._useDefaultCacheabilityPluginIfNeeded();const s=await t.fetch(e);if(!await t.cachePut(e,s.clone()))throw new l("bad-precaching-response",{url:e.url,status:s.status});return s}_useDefaultCacheabilityPluginIfNeeded(){let e=null,t=0;for(const[s,n]of this.plugins.entries())n!==d.copyRedirectedCacheableResponsesPlugin&&(n===d.defaultPrecacheCacheabilityPlugin&&(e=s),n.cacheWillUpdate&&t++);t===0?this.plugins.push(d.defaultPrecacheCacheabilityPlugin):t>1&&e!==null&&this.plugins.splice(e,1)}}d.defaultPrecacheCacheabilityPlugin={async cacheWillUpdate({response:a}){return!a||a.status>=400?null:a}};d.copyRedirectedCacheableResponsesPlugin={async cacheWillUpdate({response:a}){return a.redirected?await G(a):a}};class ee{constructor({cacheName:e,plugins:t=[],fallbackToNetwork:s=!0}={}){this._urlsToCacheKeys=new Map,this._urlsToCacheModes=new Map,this._cacheKeysToIntegrities=new Map,this._strategy=new d({cacheName:U.getPrecacheName(e),plugins:[...t,new Y({precacheController:this})],fallbackToNetwork:s}),this.install=this.install.bind(this),this.activate=this.activate.bind(this)}get strategy(){return this._strategy}precache(e){this.addToCacheList(e),this._installAndActiveListenersAdded||(self.addEventListener("install",this.install),self.addEventListener("activate",this.activate),this._installAndActiveListenersAdded=!0)}addToCacheList(e){const t=[];for(const s of e){typeof s=="string"?t.push(s):s&&s.revision===void 0&&t.push(s.url);const{cacheKey:n,url:i}=J(s),r=typeof s!="string"&&s.revision?"reload":"default";if(this._urlsToCacheKeys.has(i)&&this._urlsToCacheKeys.get(i)!==n)throw new l("add-to-cache-list-conflicting-entries",{firstEntry:this._urlsToCacheKeys.get(i),secondEntry:n});if(typeof s!="string"&&s.integrity){if(this._cacheKeysToIntegrities.has(n)&&this._cacheKeysToIntegrities.get(n)!==s.integrity)throw new l("add-to-cache-list-conflicting-integrities",{url:i});this._cacheKeysToIntegrities.set(n,s.integrity)}if(this._urlsToCacheKeys.set(i,n),this._urlsToCacheModes.set(i,r),t.length>0){const c=`Workbox is precaching URLs without revision info: ${t.join(", ")}
This is generally NOT safe. Learn more at https://bit.ly/wb-precache`;console.warn(c)}}}install(e){return I(e,async()=>{const t=new X;this.strategy.plugins.push(t);for(const[i,r]of this._urlsToCacheKeys){const c=this._cacheKeysToIntegrities.get(r),o=this._urlsToCacheModes.get(i),h=new Request(i,{integrity:c,cache:o,credentials:"same-origin"});await Promise.all(this.strategy.handleAll({params:{cacheKey:r},request:h,event:e}))}const{updatedURLs:s,notUpdatedURLs:n}=t;return{updatedURLs:s,notUpdatedURLs:n}})}activate(e){return I(e,async()=>{const t=await self.caches.open(this.strategy.cacheName),s=await t.keys(),n=new Set(this._urlsToCacheKeys.values()),i=[];for(const r of s)n.has(r.url)||(await t.delete(r),i.push(r.url));return{deletedURLs:i}})}getURLsToCacheKeys(){return this._urlsToCacheKeys}getCachedURLs(){return[...this._urlsToCacheKeys.keys()]}getCacheKeyForURL(e){const t=new URL(e,location.href);return this._urlsToCacheKeys.get(t.href)}getIntegrityForCacheKey(e){return this._cacheKeysToIntegrities.get(e)}async matchPrecache(e){const t=e instanceof Request?e.url:e,s=this.getCacheKeyForURL(t);if(s)return(await self.caches.open(this.strategy.cacheName)).match(s)}createHandlerBoundToURL(e){const t=this.getCacheKeyForURL(e);if(!t)throw new l("non-precached-url",{url:e});return s=>(s.request=new Request(e),s.params=Object.assign({cacheKey:t},s.params),this.strategy.handle(s))}}let k;const R=()=>(k||(k=new ee),k);try{self["workbox:routing:7.0.0"]&&_()}catch{}const x="GET",m=a=>a&&typeof a=="object"?a:{handle:a};class p{constructor(e,t,s=x){this.handler=m(t),this.match=e,this.method=s}setCatchHandler(e){this.catchHandler=m(e)}}class te extends p{constructor(e,t,s){const n=({url:i})=>{const r=e.exec(i.href);if(r&&!(i.origin!==location.origin&&r.index!==0))return r.slice(1)};super(n,t,s)}}class se{constructor(){this._routes=new Map,this._defaultHandlerMap=new Map}get routes(){return this._routes}addFetchListener(){self.addEventListener("fetch",e=>{const{request:t}=e,s=this.handleRequest({request:t,event:e});s&&e.respondWith(s)})}addCacheListener(){self.addEventListener("message",e=>{if(e.data&&e.data.type==="CACHE_URLS"){const{payload:t}=e.data,s=Promise.all(t.urlsToCache.map(n=>{typeof n=="string"&&(n=[n]);const i=new Request(...n);return this.handleRequest({request:i,event:e})}));e.waitUntil(s),e.ports&&e.ports[0]&&s.then(()=>e.ports[0].postMessage(!0))}})}handleRequest({request:e,event:t}){const s=new URL(e.url,location.href);if(!s.protocol.startsWith("http"))return;const n=s.origin===location.origin,{params:i,route:r}=this.findMatchingRoute({event:t,request:e,sameOrigin:n,url:s});let c=r&&r.handler;const o=e.method;if(!c&&this._defaultHandlerMap.has(o)&&(c=this._defaultHandlerMap.get(o)),!c)return;let h;try{h=c.handle({url:s,request:e,event:t,params:i})}catch(u){h=Promise.reject(u)}const g=r&&r.catchHandler;return h instanceof Promise&&(this._catchHandler||g)&&(h=h.catch(async u=>{if(g)try{return await g.handle({url:s,request:e,event:t,params:i})}catch(L){L instanceof Error&&(u=L)}if(this._catchHandler)return this._catchHandler.handle({url:s,request:e,event:t});throw u})),h}findMatchingRoute({url:e,sameOrigin:t,request:s,event:n}){const i=this._routes.get(s.method)||[];for(const r of i){let c;const o=r.match({url:e,sameOrigin:t,request:s,event:n});if(o)return c=o,(Array.isArray(c)&&c.length===0||o.constructor===Object&&Object.keys(o).length===0||typeof o=="boolean")&&(c=void 0),{route:r,params:c}}return{}}setDefaultHandler(e,t=x){this._defaultHandlerMap.set(t,m(e))}setCatchHandler(e){this._catchHandler=m(e)}registerRoute(e){this._routes.has(e.method)||this._routes.set(e.method,[]),this._routes.get(e.method).push(e)}unregisterRoute(e){if(!this._routes.has(e.method))throw new l("unregister-route-but-not-found-with-method",{method:e.method});const t=this._routes.get(e.method).indexOf(e);if(t>-1)this._routes.get(e.method).splice(t,1);else throw new l("unregister-route-route-not-registered")}}let w;const ae=()=>(w||(w=new se,w.addFetchListener(),w.addCacheListener()),w);function E(a,e,t){let s;if(typeof a=="string"){const i=new URL(a,location.href),r=({url:c})=>c.href===i.href;s=new p(r,e,t)}else if(a instanceof RegExp)s=new te(a,e,t);else if(typeof a=="function")s=new p(a,e,t);else if(a instanceof p)s=a;else throw new l("unsupported-route-type",{moduleName:"workbox-routing",funcName:"registerRoute",paramName:"capture"});return ae().registerRoute(s),s}function ne(a,e=[]){for(const t of[...a.searchParams.keys()])e.some(s=>s.test(t))&&a.searchParams.delete(t);return a}function*ie(a,{ignoreURLParametersMatching:e=[/^utm_/,/^fbclid$/],directoryIndex:t="index.html",cleanURLs:s=!0,urlManipulation:n}={}){const i=new URL(a,location.href);i.hash="",yield i.href;const r=ne(i,e);if(yield r.href,t&&r.pathname.endsWith("/")){const c=new URL(r.href);c.pathname+=t,yield c.href}if(s){const c=new URL(r.href);c.pathname+=".html",yield c.href}if(n){const c=n({url:i});for(const o of c)yield o.href}}class re extends p{constructor(e,t){const s=({request:n})=>{const i=e.getURLsToCacheKeys();for(const r of ie(n.url,t)){const c=i.get(r);if(c){const o=e.getIntegrityForCacheKey(c);return{cacheKey:c,integrity:o}}}};super(s,e.strategy)}}function ce(a){const e=R(),t=new re(e,a);E(t)}function T(a){return R().getCacheKeyForURL(a)}function D(a){return R().matchPrecache(a)}function oe(a){R().precache(a)}function le(a,e){oe(a),ce(e)}class he extends p{constructor(e,{allowlist:t=[/./],denylist:s=[]}={}){super(n=>this._match(n),e),this._allowlist=t,this._denylist=s}_match({url:e,request:t}){if(t&&t.mode!=="navigate")return!1;const s=e.pathname+e.search;for(const n of this._denylist)if(n.test(s))return!1;return!!this._allowlist.some(n=>n.test(s))}}const ue={cacheWillUpdate:async({response:a})=>a.status===200||a.status===0?a:null};class fe extends N{constructor(e={}){super(e),this.plugins.some(t=>"cacheWillUpdate"in t)||this.plugins.unshift(ue),this._networkTimeoutSeconds=e.networkTimeoutSeconds||0}async _handle(e,t){const s=[],n=[];let i;if(this._networkTimeoutSeconds){const{id:o,promise:h}=this._getTimeoutPromise({request:e,logs:s,handler:t});i=o,n.push(h)}const r=this._getNetworkPromise({timeoutId:i,request:e,logs:s,handler:t});n.push(r);const c=await t.waitUntil((async()=>await t.waitUntil(Promise.race(n))||await r)());if(!c)throw new l("no-response",{url:e.url});return c}_getTimeoutPromise({request:e,logs:t,handler:s}){let n;return{promise:new Promise(r=>{n=setTimeout(async()=>{r(await s.cacheMatch(e))},this._networkTimeoutSeconds*1e3)}),id:n}}async _getNetworkPromise({timeoutId:e,request:t,logs:s,handler:n}){let i,r;try{r=await n.fetchAndCachePut(t)}catch(c){c instanceof Error&&(i=c)}return e&&clearTimeout(e),(i||!r)&&(r=await n.cacheMatch(t)),r}}class de extends N{constructor(e={}){super(e),this._networkTimeoutSeconds=e.networkTimeoutSeconds||0}async _handle(e,t){let s,n;try{const i=[t.fetch(e)];if(this._networkTimeoutSeconds){const r=j(this._networkTimeoutSeconds*1e3);i.push(r)}if(n=await Promise.race(i),!n)throw new Error(`Timed out the network response after ${this._networkTimeoutSeconds} seconds.`)}catch(i){i instanceof Error&&(s=i)}if(!n)throw new l("no-response",{url:e.url,error:s});return n}}importScripts("sw-runtime-resources-precache.js");self.skipWaiting();z();let C=[{url:".",revision:"c524d4ee5c4ad80f47c327294b2c7b8f"},{url:"VAADIN/build/FlowBootstrap-feff2646.js",revision:"86c7b60228bd60b898bd22f12bb25dd6"},{url:"VAADIN/build/FlowClient-d5d5e377.js",revision:"3759ac769d45708784fa1ea28709029d"},{url:"VAADIN/build/generated-flow-imports-2327eb6e.js",revision:"7ce23136795fc1d8857b43130b106c57"},{url:"VAADIN/build/indexhtml-16b0a4f5.js",revision:"c8aa9ecd79d2955d4603e761112cb3af"},{url:"VAADIN/build/vaadin-app-layout-37492a04-6bcb6fbd.js",revision:"7573fe5a5516fa080b5cbc2b8a2d5fc0"},{url:"VAADIN/build/vaadin-avatar-7047be31-a34cde18.js",revision:"60dbbb9adc2b6ac5e2e24abd3d416c66"},{url:"VAADIN/build/vaadin-big-decimal-field-b42c1de1-d3f62fe3.js",revision:"55826fab11f0ace2658799ad27b16428"},{url:"VAADIN/build/vaadin-button-79ad9d5f-40adfea5.js",revision:"2b1444901ad9d5d16c45ee42dca123de"},{url:"VAADIN/build/vaadin-checkbox-13797fc9-32b85f60.js",revision:"5ef99214c4d10cc1a390005905da47d1"},{url:"VAADIN/build/vaadin-checkbox-group-a9a9e85d-b51cff7b.js",revision:"4255e810869af497bf9a3d0315fc6c81"},{url:"VAADIN/build/vaadin-combo-box-9046f78f-3b144b19.js",revision:"ee4292000861f49bce37627ece091634"},{url:"VAADIN/build/vaadin-email-field-da851bcb-ad5220ab.js",revision:"b3c1a6231f9ab051e1c0bb6fed2d3a54"},{url:"VAADIN/build/vaadin-horizontal-layout-f7b1ab51-08422213.js",revision:"66cb620141e740ee21d6f4b767ad0c54"},{url:"VAADIN/build/vaadin-integer-field-6e2954cf-eba0755f.js",revision:"f5dd3c01ed22e58284b4443605fbe3cb"},{url:"VAADIN/build/vaadin-menu-bar-be33385c-1eb02b27.js",revision:"5b016f598967535e916b34c3f8db2d19"},{url:"VAADIN/build/vaadin-mobile-drag-drop-dc77d352.js",revision:"7db9aabaecd5d6ede2149ad0fe107f3c"},{url:"VAADIN/build/vaadin-number-field-31df11f5-23385431.js",revision:"c7ee071a8337e72a3b9c827695b31f39"},{url:"VAADIN/build/vaadin-password-field-49ffb113-2cd6b595.js",revision:"33fb37ad13072781863421386e001aba"},{url:"VAADIN/build/vaadin-progress-bar-3b53bb70-73bccab2.js",revision:"e35ae9a36b966dc634116a701aab0eff"},{url:"VAADIN/build/vaadin-radio-group-4a6e2cf4-32296781.js",revision:"d31bcc2cfa05a984c8c582e4a1d781b0"},{url:"VAADIN/build/vaadin-scroller-35e68818-57be8601.js",revision:"94818c1213ad166a2b059e6c8063bfa0"},{url:"VAADIN/build/vaadin-select-5d6ab45b-2929c706.js",revision:"dc51161fa6903b020a1447b69d887cb4"},{url:"VAADIN/build/vaadin-split-layout-10c9713b-938aa0f4.js",revision:"391fd973c7ed8872ce5b97c56376a9fa"},{url:"VAADIN/build/vaadin-text-area-41c5f60c-db938e0a.js",revision:"24db756e2520b29642084c931a233e78"},{url:"VAADIN/build/vaadin-text-field-e82c445d-ac97d0bc.js",revision:"4afa80a43be245aa35aa92f6c129820e"},{url:"VAADIN/build/vaadin-time-picker-2fa5314f-768e0a40.js",revision:"1749d1f814d4a678c11001c1b2ab93ed"},{url:"VAADIN/build/vaadin-vertical-layout-ff73c403-c359c222.js",revision:"5610a66e927e2c0414ea2c377ea130a5"},{url:"VAADIN/build/vaadin-virtual-list-62d4499a-d1b9b9b8.js",revision:"e119b0224e7a5e09f9b3431e9fee93bc"}],pe=C.findIndex(a=>a.url===".")>=0;var K;(K=self.additionalManifestEntries)!=null&&K.length&&C.push(...self.additionalManifestEntries.filter(a=>a.url!=="."||!pe));const ge="offline.html",be=new URL(self.registration.scope);async function we(a){const e=await a.text();return new Response(e.replace(/<base\s+href=[^>]*>/,`<base href="${self.registration.scope}">`),a)}function ye(a){return C.some(e=>T(e.url)===T(`${a}`))}let A=!1;function O(){return{async fetchDidFail(){A=!0},async fetchDidSucceed({response:a}){return A=!1,a}}}const me=new de({plugins:[O()]});new fe({plugins:[O()]});E(new he(async a=>{async function e(){const s=await D(ge);return s?we(s):void 0}function t(){return a.url.pathname===be.pathname?e():ye(a.url)?D(a.request):e()}if(!self.navigator.onLine){const s=await t();if(s)return s}try{return await me.handle(a)}catch(s){const n=await t();if(n)return n;throw s}}));le(C);self.addEventListener("message",a=>{var e;typeof a.data!="object"||!("method"in a.data)||a.data.method==="Vaadin.ServiceWorker.isConnectionLost"&&"id"in a.data&&((e=a.source)==null||e.postMessage({id:a.data.id,result:A},[]))});self.addEventListener("push",a=>{var t;const e=(t=a.data)==null?void 0:t.json();e&&self.registration.showNotification(e.title,{body:e.body})});self.addEventListener("notificationclick",a=>{a.notification.close(),a.waitUntil(Re())});async function Re(){const a=new URL("/",self.location.origin).href,t=(await self.clients.matchAll({type:"window"})).find(s=>s.url===a);return t?t.focus():self.clients.openWindow(a)}
