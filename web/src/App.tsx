import { BrowserRouter, Route, Routes} from 'react-router-dom';
import NavigationBar from './components/NavigationBar.tsx';
import Categories from './components/Categories.tsx';
import Goods from './components/Goods.tsx';

export default function App() {
    return (
        <div className="bg-gradient-to-r from-primary to-secondary">
            <BrowserRouter>
                <NavigationBar />
                <Routes>
                    <Route path="/categories" element={<Categories />} />
                    <Route path="/goods" element={<Goods />} />
                </Routes>
            </BrowserRouter>
        </div>
    );
}
