// table.tsx
import React, {useState} from 'react';
import {MDBDataTableV5} from 'mdbreact';
import '../App.css'

interface Item {
    id: number;
    name: string;
    price: number;
}

interface GoodsTableProps {
    categories: Item[];
}

const CategoryTable: React.FC<GoodsTableProps> = ({categories}) => {
    const handleDelete = async (name: string) => {
        const confirmDelete = window.confirm('Are you sure you want to delete this category?');
        if (!confirmDelete) {
            return;
        }

        try {
            const response = await fetch(`http://localhost:8000/api/categories/${name}`, {
                method: 'DELETE',
            });

            if (response.ok) {
                console.log(`Product with ID ${name} deleted successfully.`);
                window.location.reload();
            } else {
                console.error(`Failed to delete product with ID ${name}.`);
            }
        } catch (error) {
            console.error(`An error occurred while deleting product with ID ${name}.`, error);
        }
    };

    const [answerWithName] = useState('');
    const handleEdit = (name: string) => {
        const formWindow = window.open('', '_blank');
        // @ts-ignore
        formWindow.document.write(`
    <html>
    <head>
        <title>Edit Category</title>
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
            .form-button.disabled {
                background: red;
                cursor: not-allowed;
            }
        </style>
    </head>
    <body>
        <div class="form-container">
            <h2>Edit category "${name}"</h2>
            <label class="form-label" for="name">Name:</label>
            <input class="form-input" type="text" id="name" value="${name}" name="name">
            <button class="form-button" id="submit-button">Submit</button>
        </div>
                <script src="https://unpkg.com/axios/dist/axios.min.js"></script>
            <script>
            const submitButton = document.getElementById('submit-button');
             submitButton.addEventListener('click', async () => {
             const nameInput = document.getElementById('name');
             const name = nameInput.value;
            
                try {
                    
                  const response = await axios.post('http://localhost:8000/api/categories/${name}', 
                  {name: name});
                  window.location.href = 'http://localhost:5173/goods/categories'
                  
                } catch (error) {
                  console.error('Error sending GET request:', error);
                }
    
               
              });
             
             
            </script>
            </body>
           
            </html>
            
        `);

    };
    const data = {
        columns: [
            {
                label: 'Name',
                field: 'name',
                sort: 'asc',
                width: 150,

            },
            {
                label: 'Values',
                field: 'price',
                width: 100,

            },
            {
                label: 'Actions',
                field: 'actions',
                width: 100,
            },
        ],
        rows: categories.map((category) => ({
            name: category.name,
            price: category.price,
            actions: (
                <div style={{textAlign: 'center'}}>
                    <button
                        onClick={() => handleEdit(category.name)}
                        style={{
                            marginRight: '5px',
                            padding: '5px 10px',
                            fontSize: '14px',
                            background: '#3E7FFF',
                            color: '#FFF',
                            border: 'none',
                            borderRadius: '5px',
                            cursor: 'pointer',
                        }}
                    >
                        Edit
                    </button>
                    <button
                        onClick={() => handleDelete(category.name)}
                        style={{
                            padding: '5px 10px',
                            fontSize: '14px',
                            background: '#FF4136',
                            color: '#FFF',
                            border: 'none',
                            borderRadius: '5px',
                            cursor: 'pointer',
                        }}
                    >
                        Delete
                    </button>
                </div>
            ),
        })) as any[],
    };

    return (
        <div>
            <h1>Categories</h1>
            <p
                style={{
                    textAlign: 'right',
                    marginRight: '40px',
                    fontFamily: 'Montserrat',
                    fontWeight: 'bold',
                    fontSize: '20px',
                }}
            >
                {answerWithName}
            </p>

            <MDBDataTableV5 data={data}/>
        </div>
    );
};

export default CategoryTable;