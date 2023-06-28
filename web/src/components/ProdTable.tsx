import React from 'react';
import { MDBDataTableV5 } from 'mdbreact';

interface Item {
    id: number;
    name: string;
    groupName: string;
    quantity: number;
    price: number;
}

interface ProdTableProps {
    products: Item[];
}

const ProdTable: React.FC<ProdTableProps> = ({ products }) => {
    const handleDelete = async (id: number) => {
        const confirmDelete = window.confirm('Are you sure you want to delete this product?');
        if (!confirmDelete) {
            return; // Abort deletion if user cancels
        }

        try {
            const response = await fetch(`http://localhost:8000/api/good/${id}`, {
                method: 'DELETE',
            });

            if (response.ok) {
                console.log(`Product with ID ${id} deleted successfully.`);
                // Perform any additional actions if needed
            } else {
                console.error(`Failed to delete product with ID ${id}.`);
            }
        } catch (error) {
            console.error(`An error occurred while deleting product with ID ${id}.`, error);
        }
    };


    const handleEdit = (id: number) => {
        console.log(`Edit product with ID ${id}`);
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
                field: 'groupName',
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
            groupName: product.groupName,
            quantity: product.quantity,
            price: product.price,
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
