import { useState, useEffect } from 'react';
import axios from 'axios';
import ProdTable from './ProdTable.tsx';

interface Item {
    id: number;
    name: string;
    groupName: string;
    quantity: number;
    price: number;
}

export default function Goods() {
    const [products, setProducts] = useState<Item[]>([]);

    const handleAddGood = () => {

        const formWindow = window.open('', '_blank');
        // @ts-ignore
        formWindow.document.write(`
            <html>
            <head>
                <title>Add Category</title>
                <style>
                    body {
                        font-family: 'Montserrat', serif;
                        background: linear-gradient(#007bff,pink);

                    }
                    .form-container {
                        max-width: 400px;
                        margin: 20px auto;
                        padding: 20px;
                        border: 1px solid #ccc;
                        border-radius: 10px;
                        background: whitesmoke;

                    }
                    .form-label {
                        display: block;
                        margin-bottom: 10px;
                        font-size: 16px;
                    }
                    .form-input {
                        width: 100%;
                        padding: 8px;
                        font-size: 16px;
                    }
                    .form-button {
                        margin-top: 20px;
                        padding: 10px 20px;
                        font-size: 16px;
                        background: #3E7FFF;
                        color: #FFF;
                        border: none;
                        border-radius: 10px;
                        cursor: pointer;
                    }
                </style>
            </head>
            <body>
                <div class="form-container">
                    <h2>Add Category</h2>
                    <form>
                        <label class="form-label" for="name">Name:</label>
                        <input class="form-input" type="text" id="name" name="name" required>
                         
                         <label class="form-label" for="category">Category:</label>
                        <input class="form-input" type="text" id="category" name="category" required>
  
                         <label class="form-label" for="price">Price:</label>
                        <input class="form-input" type="text" id="price" name="price" required>
                        
                        <label class="form-label" for="amount">Category Name:</label>
                        <input class="form-input" type="text" id="amount" name="amount" required>
                                                                                              
                        <button class="form-button" type="submit">Submit</button>
                    </form>
                </div>
            </body>
            </html>
            
        `);

    };

    useEffect(() => {
        const fetchData = async () => {
            try {
                const response = await axios.get('http://localhost:8000/api/good/1');
                const { data } = response;
                const newProducts = data.map((item: any) => ({
                    id: item.id,
                    name: item.name,
                    groupName: item.groupName,
                    quantity: item.count,
                    price: item.price,
                }));
                setProducts(newProducts);
            } catch (error) {
                console.error('Error fetching data:', error);
            }
        };

        // Fetch data when the component mounts
        fetchData();
    }, []);

    return (
        <div className="max-w-7xl mx-auto px-2 sm:px-6 lg:px-8">
            <div className="mt-8 mb-8">
                <ProdTable products={products} />
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
                    onClick={handleAddGood}
                >
                    Add product
                </button>
            </div>
        </div>
    );
}
