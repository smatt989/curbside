import {createStore, applyMiddleware} from 'redux';
import { cleanState, getTagChoices, getTagChoicesSuccess, getTagChoicesError } from './actions';
import reducer from './reducer';
import promise from 'redux-promise';

function initStore() {
  const createStoreWithMiddleware = applyMiddleware(
    promise
  )(createStore);
  const store = createStoreWithMiddleware(reducer, window.__REDUX_DEVTOOLS_EXTENSION__ && window.__REDUX_DEVTOOLS_EXTENSION__());
  store.dispatch(cleanState());
  fetchReferenceData(store);
  return store;
}

const store = initStore();
export default store;

function fetchReferenceData(store) {
    fetchTagTypes(store)
}

function fetchTagTypes(store) {
    store.dispatch(getTagChoices())
                .then(response => {
                    if(response.error) {
                        store.dispatch(getTagChoicesError(response.error));
                        return false;
                    }

                    store.dispatch(getTagChoicesSuccess(response.payload.data));
                    return true;
                });
}