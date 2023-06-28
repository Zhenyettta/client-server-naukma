import React from 'react';
import { MDBDataTableV5 } from 'mdbreact';

interface Item {
    id: number;
    name: string;
    group: string;
    quantity: number;
    price: number;
    supplier: string;
    characteristic: string;

}

interface ProdTableProps {
    products: Item[];
}

const ProdTable: React.FC<ProdTableProps> = ({ products }) => {
    const handleDelete = async (id: number) => {
        const confirmDelete = window.confirm('Are you sure you want to delete this product?');
        if (!confirmDelete) {
            return;
        }

        try {
            const response = await fetch(`http://localhost:8000/api/good/${id}`, {
                method: 'DELETE',
            });

            if (response.ok) {
                console.log(`Product with ID ${id} deleted successfully.`);
                window.location.reload();
            } else {
                console.error(`Failed to delete product with ID ${id}.`);
            }
        } catch (error) {
            console.error(`An error occurred while deleting product with ID ${id}.`, error);
        }
    };



    const handleEdit= (id: number) => {

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
                </style>
            </head>
            <body>
                <div class="form-container">
                    <h2>Edit Product "${id}"</h2>
                    <form>
                        <label class="form-label" for="name">Name:</label>
                        <input class="form-input" type="text" id="name" name="name">
                         
                         <label class="form-label" for="category">Category:</label>
                        <input class="form-input" type="text" id="category" name="category">
  
                         <label class="form-label" for="price">Price:</label>
                        <input class="form-input" type="text" id="price" name="price">
                        
                        <label class="form-label" for="amount">Category Name:</label>
                        <input class="form-input" type="text" id="amount" name="amount">
                        
                        <label class="form-label" for="supplier">Supplier Name:</label>
                        <input class="form-input" type="text" id="supplier" name="supplier">
                        
                        <label class="form-label" for="characteristic">Characteristic:</label>
                        <input class="form-input" type="text" id="characteristic" name="characteristic">
                                                                                              
                        <button class="form-button"  type="submit">Submit</button>
                    </form>
                </div>
            </body>
            </html>
            
        `);

    };
    const data = {
        columns: [
            {
                label: 'ID',
                field: 'id',
                sort: 'asc',
                width: 150,
            },
            {
                label: 'Name',
                field: 'name',
                sort: 'asc',
                width: 150,
            },
            {
                label: 'Group Name',
                field: 'group',
                sort: 'asc',
                width: 270,
            },
            {
                label: 'Quantity',
                field: 'quantity',
                sort: 'asc',
                width: 200,
            },
            {
                label: 'Price',
                field: 'price',
                sort: 'asc',
                width: 100,
            },
            {
                label: 'Supplier',
                field: 'supplier',
                sort: 'asc',
                width: 270,
            },
            {
                label: 'Characteristic',
                field: 'characteristic',
                sort: 'asc',
                width: 150,
            },
            {
                label: 'Actions',
                field: 'actions',
                width: 100,
                attributes: {
                    style: {
                        textAlign: 'center',
                    },
                },
            },
        ],
        rows: products.map((product) => ({
            id:product.id,
            name: product.name,
            group: product.group,
            quantity: product.quantity,
            price: product.price,
            supplier: product.supplier,
            characteristic: product.characteristic,


            actions: (
                <div style={{ textAlign: 'center' }}>
                    <button
                        onClick={() => handleEdit(product.id)}
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
                        onClick={() => handleDelete(product.id)}
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
        })),
    };

    return (
        <div>
            <h1>Goods information</h1>
            <MDBDataTableV5 data={data} />
        </div>
    );
};

export default ProdTable;
