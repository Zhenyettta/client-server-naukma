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
    const handleDelete = (id: number) => {
        console.log(`Delete product with ID ${id}`);
    };
    const handleEdit = (id: number) => {
        console.log(`Edit product with ID ${id}`);
    };
    const data = {
        columns: [
            {
                label: 'Name',
                field: 'name',
                sort: 'asc',
                width: 150,
            },
        ],
        rows: categories.map((category) => ({
            name: <div style={{ marginLeft: '20px' }}>{category.name}</div>,
            actions: (
                <div style={{ textAlign: 'center' }}>
                    <button
                        onClick={() => handleEdit(category.id)}
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
                        onClick={() => handleDelete(category.id)}
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