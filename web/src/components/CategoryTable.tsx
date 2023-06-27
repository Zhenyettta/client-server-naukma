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
            name: category.name,
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
