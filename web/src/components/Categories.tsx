import { useEffect, useState } from 'react';
import axios from 'axios';
import CategoryTable from './CategoryTable.tsx';

interface Item {
    id: number;
    name: string;
    price: number;
}

export default function Categories() {
    const [categories, setCategories] = useState<Item[]>([]);

    const handleAddCategory = async () => {
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
        <h2 style="color: black; font-size: 20px; text-align: center; font-family: 'Arial', sans-serif; font-weight: bold; text-transform: uppercase;">Add Category</h2>
        <label class="form-label" for="category-name">Category Name:</label>
        <input class="form-input" type="text" id="category-name" name="category-name" required>
        <button class="form-button" id="submit-button">Submit</button>
        <script src="https://unpkg.com/axios/dist/axios.min.js"></script>
        <script>
          const submitButton = document.getElementById('submit-button');
          submitButton.addEventListener('click', async () => {
            const categoryNameInput = document.getElementById('category-name');
            const categoryName = categoryNameInput.value;

            
            try {
                
              const response = await axios.put('http://localhost:8000/api/categories', {name: categoryName});
              window.location.href = 'http://localhost:5173/goods/categories'
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

    useEffect(() => {
        const fetchData = async () => {
            try {

                const response = await axios.get('http://localhost:8000/api/categories');
                const { data } = response;
                const newCategories = data.map((item: any) => ({
                    id: item.id,
                    name: item.name,
                    price: item.price,
                }));
                setCategories(newCategories);


            } catch (error) {
                console.error('Error fetching data:', error);
            }
        };

        // Fetch data when the component mounts
        fetchData();
    }, []);

    return (
        <div className="max-w-7xl mx-auto px-2 sm:px-6 lg:px-8">

            <div>
                <span className="sr-only">Add category</span>
            </div>
            <div className="mt-8 mb-8">
                <CategoryTable categories={categories} />
            </div>


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
                onClick={handleAddCategory}
            >
                Add category
            </button>
        </div>
    );
}
