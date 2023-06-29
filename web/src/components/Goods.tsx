import { useState, useEffect } from 'react';
import axios from 'axios';
import ProdTable from './ProdTable.tsx';

interface Item {
    id: number;
    name: string;
    group: string;
    quantity: number;
    price: number;
    supplier: string;
    characteristics: string;

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
                   
                        <label class="form-label" for="name">Name:</label>
                        <input class="form-input" type="text" id="name" name="name" required>
                         
                        <label class="form-label" for="category">Category:</label>
                        <input class="form-input" type="text" id="category" name="category">
  
                        <label class="form-label" for="price">Price:</label>
                        <input class="form-input" type="text" id="price" name="price" required>
                        
                        <label class="form-label" for="amount">Amount:</label>
                        <input class="form-input" type="text" id="amount" name="amount" required>
                        
                        <label class="form-label" for="supplier">Supplier Name:</label>
                        <input class="form-input" type="text" id="supplier" name="supplier" required>
                        
                        <label class="form-label" for="characteristics">Characteristics:</label>
                        <input class="form-input" type="text" id="characteristics" name="characteristics" required>
                                                                                              
                        <button class="form-button" id = "submit-button" ">Submit</button>
                    
                </div>
                <script src="https://unpkg.com/axios/dist/axios.min.js"></script>
            <script>
             const submitButton = document.getElementById('submit-button');
             submitButton.addEventListener('click', async () => {
             const nameInput = document.getElementById('name');
             const categoryNameInput = document.getElementById('category');
             const priceInput = document.getElementById('price');
             const amountInput = document.getElementById('amount');
             const supplierInput = document.getElementById('supplier');
             const characteristicInput = document.getElementById('characteristics');
             
             const name = nameInput.value;
             const categoryName = categoryNameInput.value;
             const price = priceInput.value;
             const amount = amountInput.value;
             const supplier = supplierInput.value;
             const characteristics = characteristicInput.value;

                try {
                    
                  const response = await axios.put('http://localhost:8000/api/good', 
                  {group: categoryName, name: name, price: price, quantity: amount, supplier: supplier, characteristics: characteristics});
                    
                  // Handle the response if needed
                } catch (error) {
                  console.error('Error sending GET request:', error);
                }
    
               
              });
            </script>
            </body>
            </html>
            
        `);

    };

    const [totalSum, setTotalSum] = useState(0);

    useEffect(() => {
        const fetchData = async () => {
            try {
                const response = await axios.get('http://localhost:8000/api/good');
                const { data } = response;
                const newProducts = data.map((item: any) => ({
                    id: item.id,
                    name: item.name,
                    group: item.group,
                    quantity: item.count,
                    price: item.price,
                    supplier: item.supplier,
                    characteristics: item.characteristics,
                }));
                setProducts(newProducts);

                const sumResponse = await axios.get('http://localhost:8000/api/totalSum');
                const { totalSum } = sumResponse.data;
                setTotalSum(totalSum);

            } catch (error) {
                console.error('Error fetching data:', error);
            }
        };

        fetchData();
    }, []);

    return (
        <div className="max-w-7xl mx-auto px-2 sm:px-6 lg:px-8">

            {/* Display the totalSum */}

            <div className="flex justify-end">
                <p className="font-bold text-xl">Total Sum: {totalSum}</p>
            </div>

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
