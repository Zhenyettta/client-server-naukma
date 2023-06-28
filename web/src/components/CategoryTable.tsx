// table.tsx
import React from 'react';
import { MDBDataTableV5 } from 'mdbreact';

interface Item {
    id: number;
    name: string;
}

interface GoodsTableProps {
    categories: Item[];
}

const CategoryTable: React.FC<GoodsTableProps> = ({ categories }) => {
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
    const handleEdit = (name: string) => {
        console.log(`Edit product with ID ${name}`);
    };
    const data = {
        columns: [
            {
                label: 'Name',
                field: 'name',
                sort: 'asc',
                width: 150,
                attributes: {
                    style: {
                        textAlign: 'left',
                    },
                },
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
        rows: categories.map((category) => ({
            name: category.name,
            actions: (
                <div style={{ textAlign: 'center' }}>
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
    <MDBDataTableV5 data={data} />
    </div>
);
};

export default CategoryTable;