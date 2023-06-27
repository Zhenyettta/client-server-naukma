import React from 'react';
import { MDBDataTable } from 'mdbreact';

public interface Item {
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
    const data = {
        columns: [
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
        ],
        rows: products.map((product) => ({
            name: product.name,
            groupName: product.groupName,
            quantity: product.quantity,
            price: product.price,
        })) as any[],
    };

    return (
        <div>
            <h1>Data Table</h1>
            <MDBDataTable data={data} />
        </div>
    );
};

export default ProdTable;
