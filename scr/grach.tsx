import React from 'react';
import {
  BarChart,
  Bar,
  XAxis,
  YAxis,
  CartesianGrid,
  Tooltip,
  Legend,
  ResponsiveContainer
} from 'recharts';
import { Card, CardHeader, CardTitle, CardContent } from '@/components/ui/card';

const JavaDownloadStats = () => {
  const data = [
    {
      name: 'AdoptOpenJDK',
      downloads: 8500,
      percentageOfTotal: 28,
      info: 'Популярен среди разработчиков',
    },
    {
      name: 'Oracle Java',
      downloads: 7200,
      percentageOfTotal: 24,
      info: 'Официальный источник',
    },
    {
      name: 'Amazon Corretto',
      downloads: 4800,
      percentageOfTotal: 16,
      info: 'Выбор AWS пользователей',
    },
    {
      name: 'Microsoft OpenJDK',
      downloads: 4200,
      percentageOfTotal: 14,
      info: 'Популярен среди Windows пользователей',
    },
    {
      name: 'Azul Zulu',
      downloads: 3000,
      percentageOfTotal: 10,
      info: 'Корпоративный выбор',
    },
    {
      name: 'Red Hat OpenJDK',
      downloads: 2400,
      percentageOfTotal: 8,
      info: 'Выбор Linux пользователей',
    },
  ];

  const CustomTooltip = ({ active, payload }) => {
    if (active && payload && payload.length) {
      const data = payload[0].payload;
      return (
        <div className="bg-white p-4 border rounded-lg shadow-lg">
          <p className="font-bold">{data.name}</p>
          <p className="text-sm">Скачиваний: {data.downloads.toLocaleString()}</p>
          <p className="text-sm">Доля: {data.percentageOfTotal}%</p>
          <p className="text-sm text-gray-600">{data.info}</p>
        </div>
      );
    }
    return null;
  };

  return (
    <Card className="w-full max-w-4xl">
      <CardHeader>
        <CardTitle>Распределение скачиваний Java по источникам</CardTitle>
      </CardHeader>
      <CardContent>
        <div className="h-96 w-full">
          <ResponsiveContainer width="100%" height="100%">
            <BarChart
              data={data}
              margin={{
                top: 20,
                right: 30,
                left: 20,
                bottom: 5,
              }}
            >
              <CartesianGrid strokeDasharray="3 3" />
              <XAxis 
                dataKey="name" 
                angle={-45}
                textAnchor="end"
                height={60}
                interval={0}
              />
              <YAxis />
              <Tooltip content={<CustomTooltip />} />
              <Legend />
              <Bar 
                dataKey="downloads" 
                name="Количество скачиваний"
                fill="#3b82f6"
                radius={[4, 4, 0, 0]}
              />
            </BarChart>
          </ResponsiveContainer>
        </div>
        <div className="mt-4 text-sm text-gray-600">
          * Данные показывают относительную популярность различных источников Java для Minecraft
        </div>
      </CardContent>
    </Card>
  );
};

export default JavaDownloadStats;
