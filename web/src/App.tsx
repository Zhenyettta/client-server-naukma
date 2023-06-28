import { BrowserRouter, Route, Routes} from 'react-router-dom';
import NavigationBar from './components/NavigationBar.tsx';
import Categories from './components/Categories.tsx';
import Goods from './components/Goods.tsx';

export default function App() {
    return (
        <div style={{ minHeight: '100vh', background: 'linear-gradient(blue, 10%, pink)' }}>
            {/* <div className="bg-gradient-to-bl from-yellow-500 to-purple-500"> */}
            <BrowserRouter>
                <NavigationBar />
                <Routes>
                    <Route path="/goods/categories" element={<Categories />} />
                    <Route path="/goods" element={<Goods />} />
                </Routes>
            </BrowserRouter>
        </div>
    );
}
