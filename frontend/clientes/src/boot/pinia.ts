import { boot } from 'quasar/wrappers';
import { createPinia } from 'pinia';

const pinia = createPinia();

export default boot(({ app }) => {
    app.use(pinia);
});

// (opcional) exporte para usar fora de componentes, se precisar
export { pinia };
