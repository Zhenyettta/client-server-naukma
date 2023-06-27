import { useState, useEffect } from 'react';
import axios from 'axios';
import Search from './Search.tsx';
import ProdTable from './ProdTable.tsx';

interface Item {
    id: number;
    name: string;
    groupName: string;
    quantity: number;
    price: number;
}

export default function Goods() {
    const [users, setUsers] = useState<Item[]>([]);

    useEffect(() => {
        axios
            .get('http://localhost:8000/api/good/1')
            .then((response) => {
                const { price, name, count } = response.data;
                const newData: Item = { id: 1, name, groupName: '', quantity: count, price };
                setUsers((prevUsers) => [...prevUsers, newData]);
            })
            .catch((error) => {
                console.error('Error fetching data:', error);
            });
    }, []);

    return (
        <div className="max-w-7xl mx-auto px-2 sm:px-6 lg:px-8">
            <div className="relative flex items-center justify-between h-16">
                <h1 className="text-black font-sans text-3xl">Goods</h1>
            </div>
            <div>
                <Search />
            </div>
            <div className="mt-8 mb-8">
                <ProdTable products={users} />
            </div>
            <div>
                <span className="sr-only">Add category</span>
                <button
                    className="w-104 h-47 flex-shrink-0 flex items-center justify-center bg-blue-600 hover:bg-blue-600 text-white"
                    title="Sign In"
                    style={{
                        borderRadius: '10px',
                        background: '#3E7FFF',
                        color: '#FFF',
                        fontSize: '20px',
                        fontFamily: 'Montserrat',
                        padding: '5px 10px',
                    }}
                >
                    Add product
                </button>
            </div>
        </div>
    );
}
